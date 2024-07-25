package mhealth.login.dependencies;

import com.fxn.stash.Stash;

public class Constants {

    /*Stash Variables*/
    public static String HCW = "health_care_worker";
    public static String LOGGED_IN_USER = "LOGGED_IN_USER";
    //public static String END_POINT = "END_POINT";

   //Decommissioned
   // public static String END_POINT = "https://c4c-api.mhealthkenya.co.ke/api/";
   //live instance
    public static String END_POINT = "https://c4c-api.kenyahmis.org/api/";
    //Test Instance
   //public static String END_POINT = "https://prod.kenyahmis.org:8003/";

    //Stash.put(Constants.END_POINT, "https://c4c_api.mhealthkenya.org/api/");



    public static String PLACES_API_KEY = "AIzaSyBlSCHUHi9XIDiePNWUqReopqLk1X0AilU";


    /*API Variables*/
    public static String API_VERSION = "c4c_api_v0";


    //https://prod.kenyahmis.org:8003/api/facilities
    //https://prod.kenyahmis.org:8003/api/facility_departments/
    //https://prod.kenyahmis.org:8003/api/exposures/covid/new


    /*auth*/
    public static String REGISTER = "auth/signup";
    public static String LOGIN = "auth/login";
    public static String COMPLETE_PROFILE = "auth/complete_profile";
    public static String GET_PROFILE = "auth/user";
    public static String UPDATE_PROFILE = "auth/update_profile";
    public static String SEND_OTP = "auth/send_reset_otp";
    public static String RESET_PASSWORD = "auth/reset_password";



    /*resources*/
    public static String FACILITIES = "facilities";
    public static String FACILITY_DEPARTMENTS = "facility_departments/";
    public static String CADRES = "cadres";
    public static String PARTNERS = "partners";

    public static String DEVICES = "devices";


    /*checkins*/
    public static String CHECKIN_HISTORY = "check_in/history";
    public static String CHECKIN = "check_in";


    public static String FEEDBACK = "feedback";

    /*exposures*/
    public static String GET_EXPOSURES = "exposures";
    public static String REPORT_EXPOSURE = "exposures/new";
    public static String REPORT_COVID_EXPOSURE = "exposures/covid/new";
    public static String MY_COVID_EXPOSURES = "exposures/covid";


    //resources
    public static String CMES = "resources/cmes";
    public static String HCW_PROTOCOLS = "resources/hcw/protocols";
    public static String SPECIAL_RESOURCES = "resources/special";

    //broadcasts
    public static String APPROVED_BROADCASTS = "broadcasts/mobile/approved";
    public static String PENDING_BROADCASTS = "broadcasts/mobile/pending";
    public static String CREATE_BROADCAST = "broadcasts/mobile/create";
    public static String APPROVE_BROADCAST = "broadcasts/mobile/approve";

    public static String IMMUNIZATIONS = "immunizations";
    public static String NEW_IMMUNIZATIONS = "immunizations/new";




    public static String COUNTIES = "https://ushauriapi.nascop.org/locator/counties";
    public static String SUB_COUNTIES = "https://ushauriapi.nascop.org/locator/scounties?county=";
    public static String WARDS = "https://ushauriapi.nascop.org/locator/wards?scounty=";

   // public static String COUNTIES = "http://ears-covid.mhealthkenya.co.ke/api/counties";
    //public static String SUB_COUNTIES = "http://ears-covid.mhealthkenya.co.ke/api/sub/counties";
    //public static String WARDS = "https://ears-covid.mhealthkenya.co.ke/api/wards";

    public static String DISEASES = "diseases";
    public static String NASCOP_CONTACT = "0800724848";

    public static String ART_GUIDELINES = "https://www.nascop.or.ke/new-guidelines";
    public static String NASCOP_WEBSITE = "https://www.nascop.or.ke";


    public static String PORTAL_LINK = "https://eportal.uonbi.ac.ke/";
    public static String REGISTRATION_LINK = "https://docs.google.com/forms/d/e/1FAIpQLScd_ZtamUKNrQ4qN4Raf4qT-5vhdjOf4k3gYpCIsEdcnCzw3g/viewform";




}
