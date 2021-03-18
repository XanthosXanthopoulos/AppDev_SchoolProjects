package com.example.demoapp.data;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demoapp.data.model.LoggedInUser;
import com.example.demoapp.data.model.api.ApiResponse;
import com.example.demoapp.data.model.api.AuthenticationResponseModel;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ApiRoutes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserDataSource
{
    MutableLiveData<AuthenticationResponseModel> result = new MutableLiveData<>();

    public void login(String email, String password)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        JSONObject postBody = new JSONObject();

        try
        {
            postBody.put("Email", email);
            postBody.put("Password", password);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.LOGIN), postBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        result.setValue(apiResponse.getResponse());
                    }
                    else
                    {
                        System.err.println(apiResponse.getErrorMessage());
                        result.setValue(null);
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    System.err.println(error.networkResponse);
                    result.setValue(null);
                }
            });

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception e)
        {

        }
    }

    public MutableLiveData<AuthenticationResponseModel> getResult()
    {
        return result;
    }

    public void logout()
    {
        // TODO: revoke authentication
    }
}