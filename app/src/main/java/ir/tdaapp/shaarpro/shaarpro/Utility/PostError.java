package ir.tdaapp.shaarpro.shaarpro.Utility;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import ir.tdaapp.shaarpro.shaarpro.Interface.IPostError;

public class PostError {

    private IPostError iPostError;
    String Url = "";
    String TAG = "";
    JSONObject object;

    public PostError(String url, String TAG, JSONObject object, IPostError iPostError) {
        this.iPostError = iPostError;
        Url = url;
        this.TAG = TAG;
        this.object = object;

        Post();
    }

    private void Post() {

        JsonObjectRequest jsonObjReq=new JsonObjectRequest(Request.Method.POST,Url,object,response -> {
            iPostError.Get();
        },error -> {

            try {
                Log.d(TAG, error.toString());
            } catch (Exception e) {
            } finally {
                iPostError.Get();
            }

        });

        AppController.getInstance().addToRequestQueue(Policy_Volley.SetTimeOut(30000, jsonObjReq));
    }

}
