package com.example.demoapp.data.datasource;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.*;
import com.example.demoapp.data.model.api.response.*;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ApiRoutes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public LiveData<DataSourceResponse<User>> getProfileInfo(String JWToken)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        MutableLiveData<DataSourceResponse<User>> result = new MutableLiveData<>();

        try
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.PROFILE_INFO), null, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    ApiResponse<ProfileInfoResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<ProfileInfoResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        User user = new User();
                        user.setUsername(apiResponse.getResponse().getUsername());
                        user.setEmail(apiResponse.getResponse().getEmail());
                        user.setName((apiResponse.getResponse().getName()));
                        user.setSurname((apiResponse.getResponse().getSurname()));
                        user.setDescription((apiResponse.getResponse().getDescription()));
                        //user.setBirthday((apiResponse.getResponse().getBirthday()));
                        user.setCountry(apiResponse.getResponse().getCountry());
                        user.setAccountType((apiResponse.getResponse().getAccountType()));

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
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + JWToken);
                    return headers;
                }
            };

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception e)
        {

        }

        return result;
    }

    public LiveData<DataSourceResponse<Boolean>> updateProfile(ProfileInfoResponseModel profile, String JWToken)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        JSONObject postBody = new JSONObject();
        MutableLiveData<DataSourceResponse<Boolean>> result = new MutableLiveData<>();

        try
        {
            postBody.put("Username", profile.getUsername());
            postBody.put("Email", profile.getEmail());
            postBody.put("Name", profile.getName());
            postBody.put("Surname", profile.getSurname());
            postBody.put("Description", profile.getDescription());
            postBody.put("Birthday", profile.getBirthday());
            postBody.put("Country", profile.getCountry());
            postBody.put("AccountType", profile.getAccountType());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPDATE_PROFILE), postBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        result.setValue(new DataSourceResponse<>(true));
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
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + JWToken);
                    return headers;
                }
            };

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception e)
        {

        }

        return result;
    }

    public LiveData<DataSourceResponse<List<Activity>>> search(SearchQueryModel query, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Activity>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPDATE_PROFILE), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Activity>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Activity>>>(){}.getType());

                if (apiResponse.isSuccessful())
                {
                    result.setValue(new DataSourceResponse<>(apiResponse.getResponse()));
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
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = super.getHeaders();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = super.getParams();
                //TODO: Add search parameters
                return params;
            }
        };

        apiHandler.addToRequestQueue(request);

        return result;
    }
}
