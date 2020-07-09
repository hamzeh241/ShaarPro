package ir.tdaapp.shaarpro.shaarpro.Data;


import java.util.List;

import ir.tdaapp.shaarpro.shaarpro.ViewModel.VM_Image;

/**
 * Created by Diako on 6/16/2019.
 */

public class DA_Add_Home {

    //برای radio button فروش
    public static boolean rdo_Sell;

    //برای radio button اجاره
    public static boolean rdo_Rent;

    //برای radio button مسکونی
    public static boolean rdo_Residential;

    //برای radio button تجاری
    public static boolean rdo_Commercial;

    //برای radio button صنعتی
    public static boolean rdo_Industrial;

    //برای تایپ انتخاب شده
    public static int TypeHome=0;

    //برای سر تیتر Fragment Type Home
    public static String TitleProperty;

    //برای اسپینر موقعیت
    public static int cmb_Location=0;

    //برای اسپینر سال ساخت
    public static int cmb_Year_of_construction=-1;

    // برای اسپینر تعداد اتاق خواب
    public static int CountRoom=0;

    //برای ادیت تکست عنوان آگهی
    public static String txt_AdTitle;

    //برای ادیت تکست توضیحات
    public static String txt_DiscriptionHome;

    //برای ادیت تکست قیمت
    public static String txt_Price;

    //برای ادیت تکست اجاره
    public static String txt_Rent;

    //برای ادیت تکست رهن
    public static String txt_Mortgage;

    //برای ادیت تکست متراژ
    public static String txt_TheArea;

    //برای ادیت تکست موبایل
    public static String txt_Mob;

    //برای ادیت تکست تلفن
    public static String txt_Phone;

    //برای ادیت تکست آدرس فیلم
    public static String txt_VideoLink;

    //برای ادیت تکست آدرس
    public static String txt_Address;

    //برای آدرس ادیت تکست کد پستی
    public static String txt_CodePosty;

    public static int ItemId=0;

    //در اینجا مشخص می شود که منزل ویژه است یا خیر
    public static boolean Special=false;


    //لیستی از چک باکس ها در ویژگی های خانه(TypeHome)
    public static List<DA_TypeHome_Boolean> booleans;

    //لیستی از اعداد در ویژگی های خانه(TypeHome)
    public static  List<DA_TypeHome_Number> numbers;

    //لیستی از اسنیپر در ویژگی های خانه(TypeHome)
    public static List<DA_TypeHome_Select> selects;

    //لیستی از متن ها در ویژگی های خانه(TypeHome)
    public static List<DA_TypeHome_Text> texts;

    //برای لیستی از عکس ها که کاربر انتخاب کرده است
    public static List<VM_Image> Images;

    //برای وضعیت منزل 0 به معنای درحال انتظار 3 به معنای ارشیو شده و 2 به معنای تایید شده
    public static int EnumKind;

    //مشخصات کاربر
    public static String SpecificationsUser;

    public static int TempLocation=0;

    ///برای تخلیه کردن مقادیر بالا
    public static void ClearDA_AddHome() {
        texts = null;
        numbers = null;
        selects = null;
        booleans = null;
        rdo_Rent = false;
        rdo_Sell = true;
        CountRoom = 0;
        cmb_Year_of_construction = -1;
        TypeHome = 0;
        txt_AdTitle = "";
        cmb_Location = 0;
        rdo_Commercial = false;
        rdo_Industrial = false;
        rdo_Residential = false;
        TitleProperty = "";
        txt_DiscriptionHome = "";
        txt_Mob = "";
        txt_Mortgage = "";
        txt_Phone = "";
        txt_Price = "";
        txt_Rent = "";
        txt_TheArea = "";
        txt_VideoLink = "";
        txt_CodePosty = "";
        txt_Address = "";
        ItemId=0;
        Images=null;
        Special=false;
        SpecificationsUser="";
    }
}
