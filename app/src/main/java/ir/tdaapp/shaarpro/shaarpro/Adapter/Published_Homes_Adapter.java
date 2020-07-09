package ir.tdaapp.shaarpro.shaarpro.Adapter;

import android.content.Context;
import io.reactivex.annotations.NonNull;
import android.app.AlertDialog;;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.tdaapp.shaarpro.shaarpro.Data.DA_Add_Home;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_TypeHome_Boolean;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_TypeHome_Number;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_TypeHome_Select;
import ir.tdaapp.shaarpro.shaarpro.Data.DA_TypeHome_Text;
import ir.tdaapp.shaarpro.shaarpro.DataBase.Features;
import ir.tdaapp.shaarpro.shaarpro.DataBase.Location;
import ir.tdaapp.shaarpro.shaarpro.DataBase.TypeHome;
import ir.tdaapp.shaarpro.shaarpro.FragmentPage.Fragment_Edit_Home;
import ir.tdaapp.shaarpro.shaarpro.Interface.IBase;
import ir.tdaapp.shaarpro.shaarpro.MainActivity;
import ir.tdaapp.shaarpro.shaarpro.R;
import ir.tdaapp.shaarpro.shaarpro.Utility.AppController;
import ir.tdaapp.shaarpro.shaarpro.Utility.Internet;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Home;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Image;
import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Location;

/**
 * Created by Diako on 8/8/2019.
 */

public class Published_Homes_Adapter extends RecyclerView.Adapter<Published_Homes_Adapter.MyViewHolder> implements IBase {

    List<VM_Home> homes;
    Context context;
    Internet internet;
    ProgressBar progressBar;
    TypeHome typeHome;
    Location location;
    Features features;

    //برای افزودن لیستی از منزل به خانه های موجود
    public void AddHomes(List<VM_Home> homes) {
        this.homes.addAll(homes);
    }

    public Published_Homes_Adapter(List<VM_Home> homes, Context context, ProgressBar progressBar) {
        this.homes = homes;
        this.context = context;
        internet = ((MainActivity) context).internet;
        this.progressBar = progressBar;
        typeHome = ((MainActivity) context).typeHome;
        location = ((MainActivity) context).location;
        features = new Features(((MainActivity) context).dbAdapter);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_list_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.Price.setText(homes.get(position).getPrice());
        holder.Address.setText(homes.get(position).getAddress());
        holder.Discription.setText(homes.get(position).getDiscription());
        holder.Time.setText(homes.get(position).getTime());
        holder.CountRoom.setText(homes.get(position).getCountRoom());
        holder.Area.setText(homes.get(position).getArea() + " متر مربع");
        holder.CountImage.setText(homes.get(position).getCountImage());

        Glide.with(context).load(homes.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .placeholder(R.drawable.shaar_image).error(R.drawable.shaar_image).into(holder.img);


        holder.Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String url = Api + "Item/ItemPro?id=" + homes.get(position).getId();

                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //در اینجا تعیین می شود که خانه فروشی است یا اجاره 1 به معنای فروش است و 0 به معنای اجاره
                            int Target = Integer.parseInt(response.getString("enumTarget"));
                            if (Target == 0) {
                                DA_Add_Home.rdo_Rent = true;
                            } else {
                                DA_Add_Home.rdo_Sell = true;
                            }

                            //در اینجا تایپ منزل ست می شوند
                            int ParentId = typeHome.GetParent_TypeHome(Integer.parseInt(response.getString("FkType")));

                            if (ParentId != 0) {
                                if (ParentId == 1) {
                                    DA_Add_Home.rdo_Residential = true;
                                } else if (ParentId == 2) {
                                    DA_Add_Home.rdo_Commercial = true;
                                } else if (ParentId == 32) {
                                    DA_Add_Home.rdo_Industrial = true;
                                }
                            }

                            //در اینجا آی دی منزل ست می شود
                            DA_Add_Home.ItemId = Integer.parseInt(response.getString("Id"));

                            //در اینجا اگر سال ساخت نامساوی صفر باشد سال ساخت را از 1405 کم کرده و پوزیشن آن بدست می آید و در کلاس استاتیک ذخیره می شود
                            if (!response.getString("Age").equalsIgnoreCase("0"))
                                DA_Add_Home.cmb_Year_of_construction = (1405 - Integer.parseInt(response.getString("Age").toString()));

                            //در اینجا متراژ ست می شود
                            DA_Add_Home.txt_TheArea = response.getString("Area");

                            //در اینجا تایتل ست می شود
                            DA_Add_Home.txt_AdTitle = response.getString("Title");

                            DA_Add_Home.EnumKind = Integer.parseInt(response.getString("enumKind"));

                            //در اینجا توضیحات ست می شود
                            DA_Add_Home.txt_DiscriptionHome = response.getString("Description");

                            if (response.getString("Special").equalsIgnoreCase("true")) {
                                DA_Add_Home.Special = true;
                            }

                            //در اینجا اگر منزل فروشی باشد قیمت آن ست می شود و اگر اجاره ای باشد قیمت اجاره آن ست می شود
                            // و اگر خانه اجاره ای باشد رهن آن ست می شود
                            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                            df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
                            if (Target == 0) {
                                DA_Add_Home.txt_Rent =response.getString("PriceShow").replace(",","").replace(" تومان","").trim();
                                DA_Add_Home.txt_Mortgage = response.getString("MortgageShow").replace(",","").replace(" تومان","").trim();
                            } else {
                                DA_Add_Home.txt_Price =response.getString("PriceShow").replace(",","").replace(" تومان","").trim();
                            }

                            // در اینجا شماره موبایل ست می شوند
                            DA_Add_Home.txt_Mob = response.getString("CellPhone");

                            //در اینجا موقعیت مکانی ست می شود
                            if (!response.getString("FkTempLocation").equalsIgnoreCase("null"))
                                DA_Add_Home.TempLocation = Integer.parseInt(response.getString("FkTempLocation"));

                            //در اینجا شماره تلفن ست می شود
                            DA_Add_Home.txt_Phone = response.getString("Tel");

                            //در اینجا لینک ویدیو ست می شود
                            DA_Add_Home.txt_VideoLink = response.getString("VideoUrl");

                            //در اینجا آدرس ست می شود
                            DA_Add_Home.txt_Address = response.getString("Address");

                            //در اینجا کدپستی ست می شود
                            DA_Add_Home.txt_CodePosty = response.getString("Code");

                            //در اینجا تایپ منزل ست می شود
                            DA_Add_Home.TypeHome = Integer.parseInt(response.getString("FkType"));

                            //تعداد اتاق
                            DA_Add_Home.CountRoom = Integer.parseInt(response.getString("Room"));

                            DA_Add_Home.SpecificationsUser = response.getString("TblUserFullName");

                            //در اینجا پوزیشن لوکیشن کاربر ست می شود
                            List<VM_Location> locations = location.GetAll();

                            int LocationId = Integer.parseInt(response.getString("FkLocation"));

                            for (int i = 0; i < locations.size(); i++) {
                                if (locations.get(i).getId() == LocationId) {
                                    DA_Add_Home.cmb_Location = i;
                                }
                            }


                            //در اینجا آدرس و آی دی عکس در سرور گرفته می شود و در کلاس استاتیک ذخیره می شوند
                            JSONArray images = response.getJSONArray("TblItemImages");

                            DA_Add_Home.Images = new ArrayList<>();

                            for (int i = 0; i < images.length(); i++) {
                                JSONObject object = images.getJSONObject(i);

                                int Id = Integer.parseInt(object.getString("Id"));
                                String url = ApiImage + "ImageSave/" + object.getString("ImageUrl");
                                DA_Add_Home.Images.add(new VM_Image(Id, url));
                            }

                            //در اینجا ویژگی های منزل ست می شوند
                            DA_Add_Home.booleans = new ArrayList<>();
                            DA_Add_Home.selects = new ArrayList<>();
                            DA_Add_Home.texts = new ArrayList<>();
                            DA_Add_Home.numbers = new ArrayList<>();

                            JSONArray Features = response.getJSONArray("TblItemFeatuers");

                            for (int i = 0; i < Features.length(); i++) {
                                JSONObject object = Features.getJSONObject(i);

                                int Id = Integer.parseInt(object.getString("TblFeaturesId"));
                                int FkFormat = Integer.parseInt(object.getString("FkFormat"));
                                String Value = object.getString("Value");

                                if (FkFormat == 1) {
                                    DA_Add_Home.texts.add(new DA_TypeHome_Text(Id, Value));
                                } else if (FkFormat == 2) {
                                    DA_Add_Home.booleans.add(new DA_TypeHome_Boolean(Id, Boolean.valueOf(Value)));
                                } else if (FkFormat == 3) {
                                    DA_Add_Home.numbers.add(new DA_TypeHome_Number(Id, Integer.parseInt(Value)));
                                } else if (FkFormat == 4) {

                                    //در اینجا پوزیشن آیتمی که کاربر انتخاب کرده است ست می شود
                                    int position = 0;
                                    String[] options = features.GetOptions_Select(Id);
                                    for (int j = 0; j < options.length; j++) {
                                        if (options[j].equalsIgnoreCase(Value)) {
                                            position = j;
                                        }
                                    }
                                    DA_Add_Home.selects.add(new DA_TypeHome_Select(Id, Value, position));
                                }
                            }

                            //در اینجا وارد صفحه ویرایش منزل می شود
                            progressBar.setVisibility(View.GONE);
                            ((AppCompatActivity) context).getSupportFragmentManager().
                                    beginTransaction().replace(R.id.SecondFragment, new Fragment_Edit_Home()).commit();

                            ((MainActivity) context).SecondFragment.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "خطای رخ داده است لطفا مجددا امتحان کنید", Toast.LENGTH_SHORT).show();
                    }
                });

                AppController.getInstance().addToRequestQueue(request);

            }
        });
    }

    @Override
    public int getItemCount() {
        return homes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Price, Address, Discription, Time, CountRoom, Area, CountImage, Special;
        ImageView img;
        CardView Item;

        public MyViewHolder(View itemView) {
            super(itemView);

            Price = itemView.findViewById(R.id.Recycler_ListHome_lbl_Price);
            Address = itemView.findViewById(R.id.Recycler_ListHome_lbl_Address);
            Discription = itemView.findViewById(R.id.Recycler_ListHome_lbl_Discription);
            Time = itemView.findViewById(R.id.Recycler_ListHome_lbl_Time);
            CountRoom = itemView.findViewById(R.id.Recycler_ListHome_lbl_CountRoom);
            Area = itemView.findViewById(R.id.Recycler_ListHome_lbl_Aria);
            img = itemView.findViewById(R.id.img);
            CountImage = itemView.findViewById(R.id.CountImage);
            Special = itemView.findViewById(R.id.Special);
            Item = itemView.findViewById(R.id.Item);
        }

    }
}
