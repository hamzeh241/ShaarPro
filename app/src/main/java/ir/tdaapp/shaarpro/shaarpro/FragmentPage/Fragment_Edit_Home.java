package ir.tdaapp.shaarpro.shaarpro.FragmentPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import io.reactivex.annotations.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.obsez.android.lib.filechooser.ChooserDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.Adapter.ImageHomeAdapter;
import ir.tdaapp.shaarpro.shaarpro.Adapter.TypeHomeAdapter;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_Add_Home;
import ir.tdaapp.shaarpro.shaarpro.DataBase.Location;
import ir.tdaapp.shaarpro.shaarpro.DataBase.TypeHome;
import ir.tdaapp.shaarpro.shaarpro.DataBase.User;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.Utility.Base64Image;
import ir.tdaapp.shaarpro.shaarpro.Utility.CompressImage;
import ir.tdaapp.shaarpro.shaarpro.Utility.FileManger;
import ir.tdaapp.shaarpro.shaarpro.Utility.Font;
import ir.tdaapp.shaarpro.shaarpro.Utility.GetRandom;
import ir.tdaapp.shaarpro.shaarpro.Utility.Policy_Volley;
import ir.tdaapp.shaarpro.shaarpro.Utility.SaveImageToMob;
import ir.tdaapp.shaarpro.shaarpro.Utility.Validation;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Image;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

/**
 * Created by Diako on 8/10/2019.
 */

public class Fragment_Edit_Home extends Fragment implements IBase {

    RelativeLayout backall, Property_Home, Remove;
    RadioButton rdo_Sell, rdo_Rent, rdo_Residential, rdo_Commercial, rdo_Industrial;
    RecyclerView Recycler_Type_PropertyHome, recycler_add_img;
    Spinner cmb_Location, cmb_AgeHome, cmb_CountRoom, Spinner_VideoLink;
    EditText txt_AdTitle, txt_DiscriptionHome, txt_CodePosty, txt_Address, txt_Price, txt_Rent, txt_Mortgage, txt_TheArea;
    EditText txt_Mob, txt_Phone, txt_VideoLink;
    ImageView add_img_Home;
    Button btn_Call, btn_Edit, btn_Record, btn_Arshive, btn_Expectation;
    TypeHomeAdapter typeHomeAdapter_Residential, typeHomeAdapter_Commercial, typeHomeAdapter_Industrial;
    TypeHome typeHome;
    GridLayoutManager gridLayoutManager;
    TextView lbl_Rent, lbl_Mortgage, lbl_Price;
    int LocationId, TempLocationId;
    User user;
    String CellPhone, Tel;
    Validation validation;

    CompressImage compressImage;

    //برای گرفتن لوکیشن ها در دیتابیس
    Location location;

    LinearLayoutManager layoutManager_image;

    ImageHomeAdapter img_homes;

    CheckBox chk_Special;

    TextView lbl_SpecificationsUser;

    EditText lbl_Mobile, lbl_Phone;
    Font font;

    Spinner cmb_TempLocation;
    TextView lbl_TempLocation;
    FileManger _FileManger;
    Button btn_TourVisitor;
    TextView lbl_TourVisitor;
    String TourUrl = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_home, container, false);

        //در اینجا فریم لایوت اصلی را مخفی می کند
        ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.GONE);

        FindItem(view);

        AdapterTypeHome(view);

        SetDataSpinner();

        recycler_add_img.setLayoutManager(layoutManager_image);

        SetData();
        OnClick();

        font.SetFont("font/b_homa.ttf", lbl_Mobile);
        font.SetFont("font/b_homa.ttf", lbl_Phone);

        return view;
    }

    void FindItem(View view) {
        rdo_Sell = view.findViewById(R.id.rdo_Sell);
        rdo_Rent = view.findViewById(R.id.rdo_Rent);
        rdo_Residential = view.findViewById(R.id.rdo_Residential);
        rdo_Commercial = view.findViewById(R.id.rdo_Commercial);
        rdo_Industrial = view.findViewById(R.id.rdo_Industrial);

        Recycler_Type_PropertyHome = view.findViewById(R.id.Recycler_Type_PropertyHome);
        recycler_add_img = view.findViewById(R.id.recycler_add_img);

        cmb_Location = view.findViewById(R.id.cmb_Location);
        cmb_AgeHome = view.findViewById(R.id.AgeHome);
        cmb_CountRoom = view.findViewById(R.id.cmb_CountRoom);
        Spinner_VideoLink = view.findViewById(R.id.Spinner_VideoLink);

        txt_AdTitle = view.findViewById(R.id.txt_AdTitle);
        txt_DiscriptionHome = view.findViewById(R.id.txt_DiscriptionHome);
        txt_CodePosty = view.findViewById(R.id.txt_CodePosty);
        txt_Address = view.findViewById(R.id.txt_Address);
        txt_Price = view.findViewById(R.id.txt_Price);
        txt_Rent = view.findViewById(R.id.txt_Rent);
        txt_Mortgage = view.findViewById(R.id.txt_Mortgage);
        txt_TheArea = view.findViewById(R.id.txt_TheArea);
        txt_Mob = view.findViewById(R.id.txt_Mob);
        txt_Phone = view.findViewById(R.id.txt_Phone);
        txt_VideoLink = view.findViewById(R.id.txt_VideoLink);

        add_img_Home = view.findViewById(R.id.add_img_Home);

        backall = view.findViewById(R.id.backall);
        Property_Home = view.findViewById(R.id.Property_Home);
        typeHome = ((MainActivity) getActivity()).typeHome;
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        lbl_Rent = view.findViewById(R.id.lbl_Rent);
        lbl_Mortgage = view.findViewById(R.id.lbl_Mortgage);
        lbl_Price = view.findViewById(R.id.lbl_Price);

        location = ((MainActivity) getActivity()).location;

        layoutManager_image = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        compressImage = new CompressImage(320, 450, 75, getActivity());

        img_homes = new ImageHomeAdapter(getContext());

        btn_Call = view.findViewById(R.id.btn_Call);
        btn_Edit = view.findViewById(R.id.btn_Edit);

        user = ((MainActivity) getActivity()).user;

        chk_Special = view.findViewById(R.id.chk_Special);
        Remove = view.findViewById(R.id.Remove);
        btn_Record = view.findViewById(R.id.btn_Record);

        validation = new Validation();
        lbl_SpecificationsUser = view.findViewById(R.id.lbl_SpecificationsUser);

        lbl_Phone = view.findViewById(R.id.lbl_Phone);
        lbl_Mobile = view.findViewById(R.id.lbl_Mobile);
        btn_Arshive = view.findViewById(R.id.btn_Arshive);
        btn_Expectation = view.findViewById(R.id.btn_Expectation);

        font = new Font(getContext());
        cmb_TempLocation = view.findViewById(R.id.cmb_TempLocation);
        lbl_TempLocation = view.findViewById(R.id.lbl_TempLocation);
        btn_TourVisitor = view.findViewById(R.id.btn_TourVisitor);
        lbl_TourVisitor = view.findViewById(R.id.lbl_TourVisitor);
    }

    //در اینجا اطلاعات در کلاس استاتیک خوانده شده و در المنت ها جاگذاری می شوند
    void SetData() {
        rdo_Sell.setChecked(DA_Add_Home.rdo_Sell);
        rdo_Rent.setChecked(DA_Add_Home.rdo_Rent);
        rdo_Residential.setChecked(DA_Add_Home.rdo_Residential);
        rdo_Commercial.setChecked(DA_Add_Home.rdo_Commercial);
        rdo_Industrial.setChecked(DA_Add_Home.rdo_Industrial);

        txt_TheArea.setText(DA_Add_Home.txt_TheArea);
        txt_AdTitle.setText(DA_Add_Home.txt_AdTitle);
        txt_DiscriptionHome.setText(DA_Add_Home.txt_DiscriptionHome);
        txt_Price.setText(DA_Add_Home.txt_Price);
        txt_Rent.setText(DA_Add_Home.txt_Rent);
        txt_Mortgage.setText(DA_Add_Home.txt_Mortgage);
        txt_Mob.setText(DA_Add_Home.txt_Mob);
        txt_Phone.setText(DA_Add_Home.txt_Phone);
        txt_VideoLink.setText(DA_Add_Home.txt_VideoLink);
        txt_Address.setText(DA_Add_Home.txt_Address);
        txt_CodePosty.setText(DA_Add_Home.txt_CodePosty);
        cmb_Location.setSelection(DA_Add_Home.cmb_Location);
        cmb_TempLocation.setSelection(DA_Add_Home.TempLocation);
        cmb_CountRoom.setSelection(DA_Add_Home.CountRoom);
        chk_Special.setChecked(DA_Add_Home.Special);
        cmb_AgeHome.setSelection(DA_Add_Home.cmb_Year_of_construction);

        lbl_SpecificationsUser.setText(getResources().getString(R.string.SpecificationsUser) + " " + DA_Add_Home.SpecificationsUser);


        //در اینجا چک می شود که میان مسکونی و تجاری و صنعتی کدام تیک خورده است که آداپتر مربوط به آن در Recycler نمایش داده می شود
        if (DA_Add_Home.rdo_Residential) {
            Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
            Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Residential);
        }
        if (DA_Add_Home.rdo_Commercial) {
            Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
            Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Commercial);
        }
        if (DA_Add_Home.rdo_Industrial) {
            Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
            Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Industrial);
        }

        if (rdo_Sell.isChecked()) {
            lbl_Price.setVisibility(View.VISIBLE);
            txt_Price.setVisibility(View.VISIBLE);

            lbl_Rent.setVisibility(View.GONE);
            txt_Rent.setVisibility(View.GONE);

            lbl_Mortgage.setVisibility(View.GONE);
            txt_Mortgage.setVisibility(View.GONE);
        } else {
            lbl_Price.setVisibility(View.GONE);
            txt_Price.setVisibility(View.GONE);

            lbl_Rent.setVisibility(View.VISIBLE);
            txt_Rent.setVisibility(View.VISIBLE);

            lbl_Mortgage.setVisibility(View.VISIBLE);
            txt_Mortgage.setVisibility(View.VISIBLE);
        }

        if (DA_Add_Home.Images != null) {

            for (int i = 0; i < DA_Add_Home.Images.size(); i++) {
                Glide.with(getActivity()).load(DA_Add_Home.Images.get(i).getUrl())
                        .asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            img_homes.Add(resource);
                            SetImageHomes();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        }

        if (DA_Add_Home.EnumKind == 0) {
            btn_Record.setVisibility(View.VISIBLE);
        } else if (DA_Add_Home.EnumKind == 2) {
            btn_Arshive.setVisibility(View.VISIBLE);
        } else if (DA_Add_Home.EnumKind == 3) {
            btn_Expectation.setVisibility(View.VISIBLE);
        }
        CellPhone = DA_Add_Home.txt_Mob;
        Tel = DA_Add_Home.txt_Phone;
    }

    //در اینجا آداپتر های recycler مربوط به مسکونی صنعتی تجاری ساخته می شوند
    void AdapterTypeHome(View view) {
        typeHomeAdapter_Residential = new TypeHomeAdapter(getActivity(), typeHome.GetDataByParentId(1), view);
        typeHomeAdapter_Commercial = new TypeHomeAdapter(getActivity(), typeHome.GetDataByParentId(2), view);
        typeHomeAdapter_Industrial = new TypeHomeAdapter(getActivity(), typeHome.GetDataByParentId(3), view);
    }

    public void openTour() {

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(
                new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        new ChooserDialog(getActivity())
                                .withFilterRegex(false, false, ".*\\.(zip)")
                                .withResources(R.string.title_choose_file, R.string.Select, R.string.Cancel)
                                .withChosenListener(new ChooserDialog.Result() {
                                    @Override
                                    public void onChoosePath(String s, File file) {
                                        lbl_TourVisitor.setText(s);
                                        TourUrl = s;
                                    }
                                })
                                .build().show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }
        ).check();
    }

    void OnClick() {

        btn_TourVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTour();
            }
        });

        backall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DA_Add_Home.ClearDA_AddHome();
                ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).SecondFragment.setVisibility(View.GONE);
                ((MainActivity) getActivity()).SecondFragment.removeAllViews();
            }
        });

        rdo_Residential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
                Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Residential);
            }
        });

        rdo_Commercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
                Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Commercial);
            }
        });

        rdo_Industrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recycler_Type_PropertyHome.setLayoutManager(gridLayoutManager);
                Recycler_Type_PropertyHome.setAdapter(typeHomeAdapter_Industrial);
            }
        });

        rdo_Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_Rent.setVisibility(View.GONE);
                lbl_Rent.setVisibility(View.GONE);

                txt_Mortgage.setVisibility(View.GONE);
                lbl_Mortgage.setVisibility(View.GONE);

                txt_Price.setVisibility(View.VISIBLE);
                lbl_Price.setVisibility(View.VISIBLE);
            }
        });

        rdo_Rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_Rent.setVisibility(View.VISIBLE);
                lbl_Rent.setVisibility(View.VISIBLE);

                txt_Mortgage.setVisibility(View.VISIBLE);
                lbl_Mortgage.setVisibility(View.VISIBLE);

                txt_Price.setVisibility(View.GONE);
                lbl_Price.setVisibility(View.GONE);
            }
        });

        btn_Arshive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Arshive.setEnabled(false);

                String url = Api + "Item/ConfirmationItem?id=" + DA_Add_Home.ItemId + "&idpro=" + user.GetId() + "&kind=3";

                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        btn_Arshive.setEnabled(true);

                        if (response.equalsIgnoreCase("true")) {
                            Toast.makeText(getContext(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                            Fragment_Manegment_Homes manegment_homes = new Fragment_Manegment_Homes();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Page", 2);
                            manegment_homes.setArguments(bundle);

                            getActivity().getSupportFragmentManager().
                                    beginTransaction().replace(R.id.Fragment_Main, manegment_homes).commit();

                            ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.VISIBLE);
                            ((MainActivity) getActivity()).SecondFragment.setVisibility(View.GONE);
                            ((MainActivity) getActivity()).SecondFragment.removeAllViews();


                        } else {
                            Toast.makeText(getContext(), "خطای در عملیات رخ داده است", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(getActivity()).setTitle("<font color='#FF7F27'>خطا</font>")
                                .setMessage("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")
                                .setPositiveButton("<font color='#FF7F27'>باشه</font>", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        btn_Arshive.setEnabled(true);
                                    }
                                }).show();

                    }
                });

                AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));
            }
        });

        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال حذف</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false);
                progress.show();

                new AlertDialog.Builder(getContext())
                        .setTitle((Html.fromHtml("<font color='#FF7F27'>پبغام</font>")))
                        .setMessage((Html.fromHtml("<font color='#FF7F27'>" + getResources().getString(R.string.DeleteItem) + "</font>")))
                        .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بله</font>")), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String url = Api + "Item/" + DA_Add_Home.ItemId;

                                StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        progress.dismiss();

                                        if (response.equalsIgnoreCase("true")) {
                                            Toast.makeText(getContext(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                                            ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.VISIBLE);
                                            ((MainActivity) getActivity()).SecondFragment.setVisibility(View.GONE);

                                            ((MainActivity) getActivity()).SecondFragment.removeAllViews();
                                            ((MainActivity) getActivity()).ThirdFragment.removeAllViews();

                                            getActivity().getSupportFragmentManager().
                                                    beginTransaction().replace(R.id.Fragment_Main, new Fragment_Home()).commit();
                                        } else {
                                            Toast.makeText(getContext(), "خطای در عملیات رخ داده است", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        progress.dismiss();

                                        new AlertDialog.Builder(getContext())
                                                .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                                                .setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")))
                                                .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بارگیری مجدد</font>")), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                });

                                AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));
                            }
                        }).setNegativeButton((Html.fromHtml("<font color='#FF7F27'>خیر</font>")), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress.dismiss();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show();
            }
        });

        add_img_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        cmb_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VM_Location location = (VM_Location) parent.getSelectedItem();
                LocationId = location.getId();
                DA_Add_Home.cmb_Location = cmb_Location.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cmb_TempLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                VM_Location location = (VM_Location) adapterView.getSelectedItem();
                TempLocationId = location.getId();

                DA_Add_Home.TempLocation = cmb_TempLocation.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CellPhone.equalsIgnoreCase("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + CellPhone));
                    startActivity(intent);
                } else if (!Tel.equalsIgnoreCase("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + Tel));
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                            .setMessage((Html.fromHtml("<font color='#FF7F27'>" + getResources().getString(R.string.HasNotPhoneNumber) + "</font>")))
                            .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        Property_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().
                        beginTransaction().replace(R.id.ThirdFragment, new Fragment_Property_Home()).commit();

                ((MainActivity) getActivity()).ThirdFragment.setVisibility(View.VISIBLE);
            }
        });

        btn_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false);
                progress.show();

                String url = Api + "Item/ConfirmationItem?id=" + DA_Add_Home.ItemId + "&idpro=" + user.GetId() + "&kind=2";

                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();

                        if (response.equalsIgnoreCase("true")) {

                            Toast.makeText(getContext(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                            ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.VISIBLE);
                            ((MainActivity) getActivity()).SecondFragment.setVisibility(View.GONE);

                            ((MainActivity) getActivity()).SecondFragment.removeAllViews();
                            ((MainActivity) getActivity()).ThirdFragment.removeAllViews();

                            getActivity().getSupportFragmentManager().
                                    beginTransaction().replace(R.id.Fragment_Main, new Fragment_Home()).commit();
                        } else {
                            progress.dismiss();

                            Toast.makeText(getContext(), "خطای در عملیات رخ داده است", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String msg = error.getMessage();
                            progress.dismiss();

                            new AlertDialog.Builder(getContext())
                                    .setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>")))
                                    .setMessage((Html.fromHtml("<font color='#FF7F27'>" + msg + "</font>")))
                                    .setPositiveButton((Html.fromHtml("<font color='#FF7F27'>بارگیری مجدد</font>")), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } catch (Exception ex) {
                            progress.dismiss();
                        }
                    }
                });

                AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));
            }
        });

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveImageAndDataToServer();
            }
        });

        btn_Expectation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Expectation.setEnabled(false);

                String url = Api + "Item/ConfirmationItem?id=" + DA_Add_Home.ItemId + "&idpro=" + user.GetId() + "&kind=0";

                StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        btn_Expectation.setEnabled(true);

                        if (response.equalsIgnoreCase("true")) {
                            Toast.makeText(getContext(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                            Fragment_Manegment_Homes manegment_homes = new Fragment_Manegment_Homes();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Page", 1);
                            manegment_homes.setArguments(bundle);

                            getActivity().getSupportFragmentManager().
                                    beginTransaction().replace(R.id.Fragment_Main, manegment_homes).commit();

                            ((MainActivity) getActivity()).Fragment_Main.setVisibility(View.VISIBLE);
                            ((MainActivity) getActivity()).SecondFragment.setVisibility(View.GONE);
                            ((MainActivity) getActivity()).SecondFragment.removeAllViews();


                        } else {
                            Toast.makeText(getContext(), "خطای در عملیات رخ داده است", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(getActivity()).setTitle("<font color='#FF7F27'>خطا</font>")
                                .setMessage("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>")
                                .setPositiveButton("<font color='#FF7F27'>باشه</font>", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        btn_Expectation.setEnabled(true);
                                    }
                                }).show();

                    }
                });

                AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(TimeOutVolley, request));

            }
        });

        cmb_CountRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DA_Add_Home.CountRoom = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //در اینجا داده های اسپینر ست می شوند
    void SetDataSpinner() {
        //در اینجا اسپینر موقعیت ست می شوند
        ArrayAdapter<VM_Location> adapter_Location = new ArrayAdapter<VM_Location>(getContext(), android.R.layout.simple_spinner_dropdown_item, location.GetAll_TypeArrayList());
        cmb_Location.setAdapter(adapter_Location);

        cmb_TempLocation.setAdapter(adapter_Location);

        //در اینجا مقادیر سال ساخت ست می شوند
        List<String> lstAgeHome = new ArrayList<>();
        for (int i = 1405; i >= 1300; i--) {
            lstAgeHome.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter_AgeHome = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lstAgeHome);
        cmb_AgeHome.setAdapter(adapter_AgeHome);

        //در اینجا مقدار سال ساخت در اسپینر سال ساخت ست می شود
        if (DA_Add_Home.cmb_Year_of_construction != -1)
            cmb_AgeHome.setSelection(DA_Add_Home.cmb_Year_of_construction);
        else
            cmb_AgeHome.setSelection(0);

        //در اینجا مفادیر تعداد اتاق ست می شوند
        String[] CountRoomData = new String[10];
        for (int i = 0; i < CountRoomData.length; i++) {
            if (i == 0) {
                CountRoomData[i] = "بدون اتاق";
            } else {
                CountRoomData[i] = String.valueOf(i);
            }
        }
        ArrayAdapter<String> adapter_CountRoom = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, CountRoomData);
        cmb_CountRoom.setAdapter(adapter_CountRoom);

        //در اینجا تعداد اتاقی که کاربر انتخاب کرده است ست می شود
        cmb_CountRoom.setSelection(DA_Add_Home.CountRoom);

        //در اینجا مقادیر لینک ویدیو ست می شوند
        ArrayAdapter<CharSequence> adapter_LinkVideo = ArrayAdapter.createFromResource(getActivity(), R.array.ListVideoApp, R.layout.support_simple_spinner_dropdown_item);
        adapter_LinkVideo.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner_VideoLink.setAdapter(adapter_LinkVideo);
    }


    //در اینجا اگر کاربر بخواهد یک عکس را انتخاب کند در اینجا نشان می دهد که از کدام راه یک عکس انتخاب کند مثل دوربین یا گالری
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
                    img_homes.Add(b);
                    SetImageHomes();

                    if (DA_Add_Home.Images == null)
                        DA_Add_Home.Images = new ArrayList<>();

                    String Name = String.valueOf(GetRandom.GetLong()) + ".jpg";
                    DA_Add_Home.Images.add(new VM_Image(1, SaveImageToMob.SaveImageToSdCard(Name, b)));

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
                img_homes.Add(b);
                SetImageHomes();

                if (DA_Add_Home.Images == null)
                    DA_Add_Home.Images = new ArrayList<>();

                String Name = String.valueOf(GetRandom.GetLong()) + ".jpg";
                DA_Add_Home.Images.add(new VM_Image(1, SaveImageToMob.SaveImageToSdCard(Name, b)));
            }
        }
    }

    void SetImageHomes() {
        recycler_add_img.setAdapter(img_homes);
    }

    //در اینجا چک می شود که تکست باکس ها valid باشند
    boolean CheckValid() {
        boolean valid = true;

        if (validation.Required(txt_AdTitle, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (validation.Required(txt_DiscriptionHome, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (validation.Required(txt_Address, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (validation.Required(txt_CodePosty, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (validation.Required(txt_Mob, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (validation.Required(txt_TheArea, getResources().getString(R.string.ThisFieldNotEmpty))) {
            valid = false;
        }

        if (rdo_Sell.isChecked()) {
            if (validation.Required(txt_Price, getResources().getString(R.string.ThisFieldNotEmpty))) {
                valid = false;
            }
        }

        if (rdo_Rent.isChecked()) {
            if (validation.Required(txt_Mortgage, getResources().getString(R.string.ThisFieldNotEmpty))) {
                valid = false;
            }

            if (validation.Required(txt_Rent, getResources().getString(R.string.ThisFieldNotEmpty))) {
                valid = false;
            }
        }
        if (!valid)
            return valid;

        if (validation.StringLength(txt_AdTitle, 3, 100, "تعداد کاراکتر باید بین 3 تا 100 کاراکتر باشد")) {
            valid = false;
        }

        if (validation.StringLength(txt_CodePosty, 3, 50, "تعداد کاراکتر باید بین 3 تا 50 کاراکتر باشد")) {
            valid = false;
        }

        if (validation.MinValue(txt_TheArea, "متراژ باید حداقل یک متر باشد", 1)) {
            valid = false;
        }

        if (validation.ValidMobile(txt_Mob, "شماره موبایل صحیح نمی باشد")) {
            valid = false;
        }

        if (validation.MaxLength(txt_Phone, "شماره تلفن صحیح نمی باشد", 15)) {
            valid = false;
        }

        if (validation.MinLength(txt_DiscriptionHome, "تعداد کاراکتر باید حداقل 3 کاراکنر باشد", 3)) {
            valid = false;
        }

        return valid;
    }

    //در اینجا عکس ها رادر سرور ذخیره می کند و نام آن ها را برگشت می دهد
    void SaveImageAndDataToServer() {

        if (CheckValid()) {
            if (rdo_Commercial.isChecked() || rdo_Industrial.isChecked() || rdo_Residential.isChecked()) {

                btn_Edit.setEnabled(false);

                //در اینجا progressbar لودینگ نمایش داده می شود
                final ProgressDialog progress = new ProgressDialog(getActivity());
                progress.setTitle((Html.fromHtml("<font color='#FF7F27'>در حال ارسال</font>")));
                progress.setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا منتظر بمانید</font>")));
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();

                new Thread(() -> {
                    //در اینجا تور مجازی ست می شود

                    String UrlTour = Api + "User/PostFile3D";
                    String TourId = "";

                    if (!TourUrl.equalsIgnoreCase("")) {

                        try {

                            _FileManger = new FileManger(UrlTour);
                            TourId = _FileManger.uploadFile(TourUrl).replace("\"", "");

                        } catch (Exception e) {
                            TourId = "";
                        }

                    }

                    //در اینجا چک می کند که تور مجازی به درستی ارسال شده باشد یا اصلا کاربر تور مجازی ست نکرده باشد که اگر شرط درست باشد ادامه عملیات انجام می شود
                    //if ((TourId.length() > 10 && TourId.length() < 20) || TourId.equalsIgnoreCase("")) {

                        //در اینجا عکس ها ست می شوند
                        String UrlImage = Api + "User/PostFile";
                        _FileManger = new FileManger(UrlImage);

                        List<String> vals = new ArrayList<>();

                        for (Bitmap i : img_homes.GetValues()) {

                            try {

                                String Name = SaveImageToMob.SaveImageToSdCard("fgsdssdffh.jpg", i);
                                File file = new File(Name);

                                vals.add(_FileManger.uploadFile(file.getPath()).replace("\"", ""));

                                file.delete();

                            } catch (Exception e) {

                            }
                        }

                        SaveData(vals, progress, TourId);
                   // } else {
//                        getActivity().runOnUiThread(() -> {
//
//                            progress.dismiss();
//                            btn_Edit.setEnabled(true);
//                            new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا سرعت اینترنت یا حجم فایل خود را چک نمایید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
//
//                        });

                   // }
                }).start();


            } else {
                btn_Edit.setEnabled(true);
                new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>لطفا ویژگی های منزل خود را وارد نمایید</font>"))).setPositiveButton((Html.fromHtml("<font color='#FF7F27'>باشه</font>")), null).show();
            }
        } else {
            btn_Edit.setEnabled(true);
            Toast.makeText(getContext(), "لطفا اطلاعات را کامل وارد نمایید", Toast.LENGTH_SHORT).show();
        }
    }

    void SaveData(List<String> val, ProgressDialog progress, String TourId) {

        JSONObject object = new JSONObject();

        try {

            object.put("Title", txt_AdTitle.getText());

            String Price;

            if (rdo_Sell.isChecked())
                Price = txt_Price.getText().toString();
            else
                Price = txt_Rent.getText().toString();

            object.put("Price", Price);

            object.put("Id", DA_Add_Home.ItemId);

            object.put("Area", txt_TheArea.getText());

            object.put("CellPhone", txt_Mob.getText().toString());

            object.put("Tel", txt_Phone.getText().toString());

            object.put("VideoUrl", txt_VideoLink.getText().toString());

            object.put("Room", DA_Add_Home.CountRoom);

            object.put("Special", chk_Special.isChecked());

            object.put("TourId", TourId);

            String Age;
            if (DA_Add_Home.cmb_Year_of_construction != -1)
                Age = cmb_AgeHome.getItemAtPosition(DA_Add_Home.cmb_Year_of_construction).toString();
            else
                Age = "0";

            object.put("Age", Age);

            object.put("Code", txt_CodePosty.getText().toString());

            object.put("Address", txt_Address.getText().toString());

            int enumTarget;

            if (rdo_Sell.isChecked()) {
                enumTarget = 1;
            } else {
                enumTarget = 0;
            }

            object.put("enumTarget", enumTarget);

            object.put("enumKind", 2);

            object.put("FkType", DA_Add_Home.TypeHome);

            object.put("FkLocation", LocationId);

            object.put("FkTempLocation", TempLocationId);

            object.put("FkUserPro", user.GetId());

            String Mortgage;
            if (rdo_Rent.isChecked()) {
                Mortgage = txt_Mortgage.getText().toString();
            } else {
                Mortgage = "0";
            }
            object.put("Mortgage", Mortgage);

            object.put("Description", txt_DiscriptionHome.getText());

            object.put("ImageCount", val.size());

            JSONArray images = new JSONArray();
            for (int i = 0; i < val.size(); i++) {

                JSONObject image = new JSONObject();
                image.put("ImageUrl", val.get(i));
                image.put("Id", i);
                image.put("FkItem", DA_Add_Home.ItemId);

                images.put(image);
            }

            object.put("TblItemImagesTemp", images);

            //در اینجا ویژگی های منزل ست می شوند
            JSONArray Features = new JSONArray();

            for (int i = 0; i < DA_Add_Home.selects.size(); i++) {

                JSONObject home = new JSONObject();
                home.put("FkItem", DA_Add_Home.ItemId);
                home.put("FkFeatures", DA_Add_Home.selects.get(i).getId());
                home.put("FkFormat", 4);
                home.put("Value", DA_Add_Home.selects.get(i).getVal());

                Features.put(home);
            }

            for (int i = 0; i < DA_Add_Home.numbers.size(); i++) {
                JSONObject home = new JSONObject();
                home.put("FkItem", DA_Add_Home.ItemId);
                home.put("FkFeatures", DA_Add_Home.numbers.get(i).getId());
                home.put("FkFormat", 3);
                home.put("Value", DA_Add_Home.numbers.get(i).getVal());

                Features.put(home);
            }

            for (int i = 0; i < DA_Add_Home.booleans.size(); i++) {
                JSONObject home = new JSONObject();
                home.put("FkItem", DA_Add_Home.ItemId);
                home.put("FkFeatures", DA_Add_Home.booleans.get(i).getId());
                home.put("FkFormat", 2);
                home.put("Value", DA_Add_Home.booleans.get(i).getVal());

                Features.put(home);
            }

            for (int i = 0; i < DA_Add_Home.texts.size(); i++) {
                JSONObject home = new JSONObject();
                home.put("FkItem", DA_Add_Home.ItemId);
                home.put("FkFeatures", DA_Add_Home.texts.get(i).getId());
                home.put("FkFormat", 1);
                home.put("Value", DA_Add_Home.texts.get(i).getVal());

                Features.put(home);
            }

            object.put("TblItemFeatuersSend", Features);


            String url = Api + "Item";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                        btn_Edit.setEnabled(true);

                        String Status = response.getString("Status").toString();
                        String Title = response.getString("Titel");

                        if (Status.equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), Title, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    btn_Edit.setEnabled(true);

                    new AlertDialog.Builder(getActivity()).setTitle((Html.fromHtml("<font color='#FF7F27'>خطا</font>"))).setMessage((Html.fromHtml("<font color='#FF7F27'>اینترنت شما بسیار ضعیف است لطفا مجددا امتحان کنید</font>"))).show();
                }
            });

            AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOutToPost(request));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
