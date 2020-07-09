package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.Utility.Base64Image;
import ir.tdaapp.shaarpro.shaarpro.Utility.CompressImage;
import ir.tdaapp.shaarpro.shaarpro.Utility.Internet;
import ir.tdaapp.shaarpro.shaarpro.Utility.Policy_Volley;
import ir.tdaapp.shaarpro.shaarpro.Utility.PostError;
import ir.tdaapp.shaarpro.shaarpro.Utility.ReplaceData;
import ir.tdaapp.shaarpro.shaarpro.Utility.SaveImageToMob;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

/**
 * Created by Diako on 8/3/2019.
 */

public class Fragment_Add_Account extends Fragment implements IBase {

    public static final String TAG = "Fragment_Add_Account";

    EditText txt_Mob, txt_FullName, txt_Email;
    Button btn_Done;
    Internet internet;
    CompressImage compressImage;
    CircleImageView profile_image;
    RelativeLayout back;
    ReplaceData replaceData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);

        FindItem(view);
        OnClick();

        return view;
    }

    void FindItem(View view) {
        txt_Mob = view.findViewById(R.id.txt_Mob);
        btn_Done = view.findViewById(R.id.btn_Done);
        txt_FullName = view.findViewById(R.id.txt_FullName);
        txt_Email = view.findViewById(R.id.Email);
        internet = ((MainActivity) getActivity()).internet;
        compressImage = new CompressImage(400, 350, 75, getActivity());
        profile_image = view.findViewById(R.id.profile_image);
        back = view.findViewById(R.id.back);
        replaceData = new ReplaceData();
    }

    void OnClick() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().
                        beginTransaction().setCustomAnimations(R.anim.fadein, R.anim.fadeout).replace(R.id.Fragment_Main, new Fragment_Login_Home()).commit();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        txt_Mob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_Mob.setGravity(Gravity.LEFT);
                    txt_Mob.setHint("");
                } else {
                    if (txt_Mob.getText().toString().equalsIgnoreCase("")) {
                        txt_Mob.setGravity(Gravity.RIGHT);
                        txt_Mob.setHint("شماره موبایل خود را وارد نمایید");
                    } else {
                        txt_Mob.setGravity(Gravity.LEFT);
                    }
                }
            }
        });

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Done.setEnabled(false);

                if (internet.HaveNetworkConnection()) {

                    //در اینجا چک می شود که ادیت تکست ها خالی نباشد
                    if (txt_FullName.getText().toString().equalsIgnoreCase("") ||
                            txt_Mob.getText().toString().equalsIgnoreCase("")) {
                        new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا اطلاعات را کامل وارد نمایید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>متوجه شدم</font>")), null).show();
                        btn_Done.setEnabled(true);
                    } else {

                        //در اینجا progressbar لودینگ نمایش داده می شود
                        final ProgressDialog progress = new ProgressDialog(getActivity());
                        progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                        progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();

                        String url2 = Api + "User/UserProNew";


                        Bitmap img = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                        final String Image = Base64Image.BitmapToBase64(img);

                        JSONObject object = new JSONObject();
                        try {
                            object.put("FullName", txt_FullName.getText().toString());
                            object.put("Email", txt_Email.getText().toString());
                            object.put("CellPhone", replaceData.Number_PersianToEnglish(txt_Mob.getText().toString()));
                            object.put("Image", Image);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url2, object, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progress.dismiss();
                                try {
                                    if (response.getString("Code").equalsIgnoreCase("406")) {
                                        int Id = Integer.parseInt(response.getString("Redirect"));

                                        ((MainActivity) getActivity()).user.AddUser(Id, txt_FullName.getText().toString(), txt_Email.getText().toString(), txt_Mob.getText().toString());
                                        getActivity().getSupportFragmentManager().
                                                beginTransaction().replace(R.id.Fragment_Main, new Fragment_Wait_To_Confirm()).commit();
                                    } else {
                                        new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>" + response.getString("Titel") + "</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                String UrlError = Api + "Values";

                                JSONObject ObjectError = new JSONObject();

                                try {

                                    ObjectError.put("Title", "ShaarPro_AddAccount");
                                    ObjectError.put("ErrorMessage", new ReplaceData().ReplaceError(error.getMessage()));
                                    ObjectError.put("AndroidVersion", Build.VERSION.RELEASE + "");
                                    ObjectError.put("VersionApp", "1.0");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                new PostError(UrlError, TAG, ObjectError, () -> {

                                    btn_Done.setEnabled(true);
                                    progress.dismiss();
                                    new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>در سرور خطای رخ داده است لطفا بعدا امتحان کنید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();

                                });

                            }
                        });

                        AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));
                    }
                } else {
                    btn_Done.setEnabled(true);
                    Toast.makeText(getActivity(), getResources().getString(R.string.CheckConnectionInternet), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"دوربین", "از گالری", "انصراف"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((Html.fromHtml("<font color='#FF7F27'>عکس مورد نظر</font>")));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("دوربین")) {

                    Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).withListener(
                            new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    String fileName = "temp.jpg";
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent, 1);
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                }
                            }
                    ).check();


                } else if (options[item].equals("از گالری")) {
                    Dexter.withActivity(getActivity()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(
                            new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/jpg");
                                    startActivityForResult(intent, 2);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                }
                            }
                    ).check();


                } else if (options[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getActivity().getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Uri selectedImage = getCacheImagePath("temp.jpg");
                String uu = getCacheImagePath("temp.jpg").getPath();
                File f = new File(getCacheImagePath("temp.jpg").toString());
                try {
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, selectedImage);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                    Bitmap b = compressImage.Compress(decoded);

                    profile_image.setImageBitmap(b);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                final int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap b = compressImage.Compress(picturePath);
                profile_image.setImageBitmap(b);
            }
        }
    }
}
