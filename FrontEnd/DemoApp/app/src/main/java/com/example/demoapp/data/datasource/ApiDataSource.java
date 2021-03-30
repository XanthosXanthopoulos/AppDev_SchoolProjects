package com.example.demoapp.data.datasource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.*;
import com.example.demoapp.data.model.api.response.*;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ApiRoutes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

public class ApiDataSource
{
    public LiveData<DataSourceResponse<User>> login(SingInCredentialsModel singInCredentials)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        JSONObject postBody = new JSONObject();
        MutableLiveData<DataSourceResponse<User>> result = new MutableLiveData<>();

        try
        {
            postBody.put("Email", singInCredentials.getEmail());
            postBody.put("Password", singInCredentials.getPassword());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.LOGIN), postBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        User user = new User();
                        user.setUsername(apiResponse.getResponse().getUsername());
                        user.setJwToken(apiResponse.getResponse().getJwToken());

                        result.setValue(new DataSourceResponse<>(user));
                    }
                    else
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    System.err.println(error.networkResponse);
                    result.setValue(new DataSourceResponse<>("Network error"));
                }
            });

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception e)
        {

        }

        return result;
    }

    public LiveData<DataSourceResponse<User>> register(RegisterCredentialsModel registerCredentials)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        JSONObject postBody = new JSONObject();
        MutableLiveData<DataSourceResponse<User>> result = new MutableLiveData<>();

        try
        {
            postBody.put("Username", registerCredentials.getUsername());
            postBody.put("Email", registerCredentials.getEmail());
            postBody.put("Password", registerCredentials.getPassword());
            postBody.put("ConfirmPassword", registerCredentials.getConfirmPassword());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.REGISTER), postBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        User user = new User();
                        user.setUsername(apiResponse.getResponse().getUsername());
                        user.setJwToken(apiResponse.getResponse().getJwToken());

                        result.setValue(new DataSourceResponse<>(user));
                    }
                    else
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    System.err.println(error.networkResponse);
                    result.setValue(new DataSourceResponse<>("Network error"));
                }
            });

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception e)
        {

        }

        return result;
    }
}
