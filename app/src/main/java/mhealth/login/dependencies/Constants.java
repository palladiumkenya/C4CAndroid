package mhealth.login.dependencies;

public class Constants {

    /*Stash Variables*/
    public static String HCW = "health_care_worker";
    public static String LOGGED_IN_USER = "LOGGED_IN_USER";
    public static String END_POINT = "END_POINT";

    public static String PLACES_API_KEY = "AIzaSyBlSCHUHi9XIDiePNWUqReopqLk1X0AilU";


    /*API Variables*/
    public static String API_VERSION = "c4c_api_v0";

    /*auth*/
    public static String REGISTER = "auth/signup";
    public static String LOGIN = "auth/login";
    public static String COMPLETE_PROFILE = "auth/complete_profile";
    public static String GET_PROFILE = "auth/user";
    public static String UPDATE_PROFILE = "auth/update_profile";



    /*resources*/
    public static String FACILITIES = "facilities";
    public static String FACILITY_DEPARTMENTS = "facility_departments/";
    public static String CADRES = "cadres";
    public static String DEVICES = "devices";


    /*checkins*/
    public static String CHECKIN_HISTORY = "check_in/history";
    public static String CHECKIN = "check_in";


    public static String FEEDBACK = "feedback";

    /*exposures*/
    public static String GET_EXPOSURES = "exposures";
    public static String REPORT_EXPOSURE = "exposures/new";


    //resources
    public static String CMES = "resources/cmes";
    public static String HCW_PROTOCOLS = "resources/hcw/protocols";

    //broadcasts
    public static String APPROVED_BROADCASTS = "broadcasts/mobile/approved";
    public static String PENDING_BROADCASTS = "broadcasts/mobile/pending";
    public static String CREATE_BROADCAST = "broadcasts/mobile/create";
    public static String APPROVE_BROADCAST = "broadcasts/mobile/approve";

    public static String IMMUNIZATIONS = "immunizations";
    public static String NEW_IMMUNIZATIONS = "immunizations/new";

    public static String DISEASES = "diseases";
    public static String NASCOP_CONTACT = "0800724848";



    public static final String[] SPINNERLISTWHERE = {"Medical Ward", "Surgical Ward", "Theater", "Maternity", "Dental Clinic", "OP/MCH", "Laundry", "Laboratory","Other"};
    public static final String[] SPINNERLISTTYPE = {"Needle Stick", "Cuts", "Splash on Mucosa", "Non-intact Skin", "Bite", "Other"};
    public static final String[] SPINNERLISTDEVICE = {
            "Syringe/Needle IM/SC Injection",
            "Syring/Needle Blood Drawing",
            "Phlebotomy Needle/ Vacuum set",
            "IV Catheter/Canula",
            "Needle on IV Line",
            "Unused Needle",
            "Lancet",
            "Suture Needle",
            "Scalpel",
            "Capillary Tube",
            "Glass Slide",
            "Pippete Tip",
            "Other"
    };










}
