package com.example.demoapp.data.datasource;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.*;
import com.example.demoapp.data.model.api.response.*;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ApiRoutes;
import com.example.demoapp.util.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
                        user.setProfileImageID(apiResponse.getResponse().getProfileImageID());

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
                    ApiResponse<User> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<User>>(){}.getType());

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

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                result.setValue(new DataSourceResponse<>(false));
            }
        };

        imageUpload = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    postBody.put("ProfileImageID", response);
                    postBody.put("Name", profile.getName());
                    postBody.put("Surname", profile.getSurname());
                    postBody.put("Description", profile.getDescription());
                    postBody.put("Birthday", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(profile.getBirthday()));
                    postBody.put("Country", profile.getCountry().code);
                    postBody.put("AccountType", profile.getAccountType().value);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPDATE_PROFILE), postBody, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        ApiResponse<Boolean> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<Boolean>>(){}.getType());

                        if (apiResponse.isSuccessful())
                        {
                            result.setValue(new DataSourceResponse<>(true));
                        }
                        else
                        {
                            result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                        }
                    }
                }, errorListener)
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
        };

        Request initialRequest = null;

        if (profile.getProfileImage() != null)
        {
            initialRequest = new ImageUploadRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_UPLOAD), profile.getProfileImage(), JWToken, imageUpload, errorListener);
        }
        else
        {
            try
            {
                postBody.put("Name", profile.getName());
                postBody.put("Surname", profile.getSurname());
                postBody.put("Description", profile.getDescription());
                postBody.put("Birthday", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(profile.getBirthday()));
                postBody.put("Country", profile.getCountry().code);
                postBody.put("AccountType", profile.getAccountType().value);

                initialRequest = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPDATE_PROFILE), postBody, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        ApiResponse<Boolean> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<Boolean>>(){}.getType());

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
            }
            catch (Exception e)
            {

            }
        }

        apiHandler.addToRequestQueue(initialRequest);

        return result;
    }

    public LiveData<DataSourceResponse<List<Item>>> searchActivities(SearchQueryModel query, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("query", query.getQuery());
        if (query.getCountry() != null && !query.getCountry().isEmpty())
        {
            params.put("country", String.valueOf(Country.lookupByLabel(query.getCountry()).code));
        }
        params.put("city", query.getCity());

        Response.Listener<JSONObject> searchResponse = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Activity>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Activity>>>(){}.getType());

                if (apiResponse.isSuccessful())
                {
                    List<Item> items = new ArrayList<>(apiResponse.getResponse());
                    result.setValue(new DataSourceResponse<>(items));
                }
                else
                {
                    result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network error"));
            }
        };

        if (query.getCity() != null && !query.getCity().isEmpty())
        {
            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "255230665c9249b28259b49dacc2c198");
            geoParams.put("q", query.getCity());
            JsonObjectRequest init = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                        params.put("latitude", String.valueOf(coordinates.getDouble("lat")));
                        params.put("longtitude", String.valueOf(coordinates.getDouble("lng")));
                        params.put("radius", String.valueOf(query.getRadius()));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, searchResponse, errorListener)
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
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, searchResponse, errorListener)
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
            });

            apiHandler.addToRequestQueue(init);
        }
        else
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, searchResponse, errorListener)
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

        return result;
    }

    public LiveData<DataSourceResponse<List<Item>>> searchPosts(SearchQueryModel query, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("query", query.getQuery());
        if (query.getCountry() != null && !query.getCountry().isEmpty())
        {
            params.put("country", String.valueOf(Country.lookupByLabel(query.getCountry()).code));
        }
        params.put("city", query.getCity());

        Response.Listener<JSONObject> searchResponse = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Post>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Post>>>(){}.getType());

                if (apiResponse.isSuccessful())
                {
                    List<Item> items = new ArrayList<>(apiResponse.getResponse());
                    result.setValue(new DataSourceResponse<>(items));
                }
                else
                {
                    result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network error"));
            }
        };

        if (query.getCity() != null && !query.getCity().isEmpty())
        {
            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "255230665c9249b28259b49dacc2c198");
            geoParams.put("q", query.getCity());
            JsonObjectRequest init = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                        params.put("latitude", String.valueOf(coordinates.getDouble("lat")));
                        params.put("longtitude", String.valueOf(coordinates.getDouble("lng")));
                        params.put("radius", String.valueOf(query.getRadius()));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_POST, params), null, searchResponse, errorListener)
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
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_POST, params), null, searchResponse, errorListener)
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
            });

            apiHandler.addToRequestQueue(init);
        }
        else
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_POST, params), null, searchResponse, errorListener)
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

        return result;
    }

    public LiveData<DataSourceResponse<List<Item>>> getFeed(String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.FEED), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Post>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Post>>>(){}.getType());

                if (apiResponse.isSuccessful())
                {
                    List<Item> items = new ArrayList<>(apiResponse.getResponse().size());
                    items.addAll(apiResponse.getResponse());
                    result.setValue(new DataSourceResponse<>(items));
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

        return result;
    }

    public LiveData<DataSourceResponse<Post>> getPost(int postID, String JWToken)
    {
        MutableLiveData<DataSourceResponse<Post>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("postID", String.valueOf(postID));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_POST, params), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<Post> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<Post>>(){}.getType());

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
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }
        };

        apiHandler.addToRequestQueue(request);


        return result;
    }

    public LiveData<DataSourceResponse<Boolean>> uploadPost(Post post, String JWToken)
    {
        MutableLiveData<DataSourceResponse<Boolean>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        LinkedList<Uri> pendingImages = new LinkedList<>();
        post.getImages().forEach(path -> pendingImages.add(Uri.parse(path)));
        LinkedList<Activity> pendingActivities = new LinkedList<>(post.getActivities());

        LinkedList<String> imageIDs = new LinkedList<>();
        LinkedList<String> activityIDs = new LinkedList<>();

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                result.setValue(new DataSourceResponse<>(false));
            }
        };

        postUpload = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                result.setValue(new DataSourceResponse<>(true));
            }
        };

        activityUpload = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<String> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<String>>(){}.getType());
                activityIDs.add(apiResponse.getResponse());
                JsonObjectRequest request;

                if (pendingActivities.isEmpty())
                {
                    JSONObject postSubmitModel = new JSONObject();
                    JSONArray images = new JSONArray();
                    JSONArray activities = new JSONArray();
                    try
                    {
                        postSubmitModel.put("Title", post.getTitle());
                        postSubmitModel.put("Description", post.getDescription());
                        postSubmitModel.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(post.getDate()));

                        for (String image : imageIDs)
                        {
                            images.put(image);
                        }

                        for (String activity : activityIDs)
                        {
                            activities.put(activity);
                        }

                        postSubmitModel.put("Images", images);
                        postSubmitModel.put("Activities", activities);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_POST), postSubmitModel, postUpload, errorListener)
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError
                        {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + JWToken);
                            return headers;
                        }
                    };
                }
                else
                {
                    Activity activity = pendingActivities.pop();
                    JSONObject activitySubmitModel = new JSONObject();
                    try
                    {
                        activitySubmitModel.put("Title", activity.getTitle());
                        activitySubmitModel.put("Description", activity.getDescription());
                        activitySubmitModel.put("Address", activity.getAddress());
                        activitySubmitModel.put("Country", activity.getCountry().code);
                        activitySubmitModel.put("Tags", activity.getTags());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    HashMap<String, String> geoParams = new HashMap<>();
                    geoParams.put("key", "255230665c9249b28259b49dacc2c198");
                    geoParams.put("q", activity.getAddress() + " ," + activity.getCountry().label);
                    request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                                activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                                activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
                    });
                }

                apiHandler.addToRequestQueue(request);
            }
        };

        imageUpload = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Request request;
                imageIDs.add(response);

                if (pendingImages.isEmpty())
                {
                    if (pendingActivities.isEmpty())
                    {
                        JSONObject postSubmitModel = new JSONObject();
                        JSONArray images = new JSONArray();
                        JSONArray activities = new JSONArray();

                        try
                        {
                            postSubmitModel.put("Title", post.getTitle());
                            postSubmitModel.put("Description", post.getDescription());
                            postSubmitModel.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(post.getDate()));

                            for (String image : imageIDs)
                            {
                                images.put(image);
                            }

                            for (String activity : activityIDs)
                            {
                                activities.put(activity);
                            }

                            postSubmitModel.put("Images", images);
                            postSubmitModel.put("Activities", activities);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_POST), postSubmitModel, postUpload, errorListener)
                        {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError
                            {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + JWToken);
                                return headers;
                            }
                        };
                    }
                    else
                    {
                        Activity activity = pendingActivities.pop();
                        JSONObject activitySubmitModel = new JSONObject();
                        try
                        {
                            activitySubmitModel.put("Title", activity.getTitle());
                            activitySubmitModel.put("Description", activity.getDescription());
                            activitySubmitModel.put("Address", activity.getAddress());
                            activitySubmitModel.put("Country", activity.getCountry().code);
                            activitySubmitModel.put("Tags", activity.getTags());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        HashMap<String, String> geoParams = new HashMap<>();
                        geoParams.put("key", "255230665c9249b28259b49dacc2c198");
                        geoParams.put("q", activity.getAddress() + " ," + activity.getCountry().label);
                        request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try
                                {
                                    JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                                    activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                                    activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
                        }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
                        });
                    }
                }
                else
                {
                    request = new ImageUploadRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_UPLOAD), pendingImages.pop(), JWToken, imageUpload, errorListener);
                }

                apiHandler.addToRequestQueue(request);
            }
        };

        while (!pendingActivities.isEmpty() && !pendingActivities.peek().getId().isEmpty())
        {
            activityIDs.add(pendingActivities.pop().getId());
        }

        Request<?> initialRequest;
        if (!pendingImages.isEmpty())
        {
            initialRequest = new ImageUploadRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_UPLOAD), pendingImages.pop(), JWToken, imageUpload, errorListener);
        }
        else if (!pendingActivities.isEmpty())
        {
            Activity activity = pendingActivities.pop();
            JSONObject activitySubmitModel = new JSONObject();
            try
            {
                activitySubmitModel.put("Title", activity.getTitle());
                activitySubmitModel.put("Description", activity.getDescription());
                activitySubmitModel.put("Address", activity.getAddress());
                activitySubmitModel.put("Country", activity.getCountry().code);
                activitySubmitModel.put("Tags", activity.getTags());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "255230665c9249b28259b49dacc2c198");
            geoParams.put("q", activity.getAddress() + " ," + activity.getCountry().label);
            initialRequest = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                        activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                        activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
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
            });
        }
        else
        {
            JSONObject postSubmitModel = new JSONObject();
            JSONArray images = new JSONArray();
            JSONArray activities = new JSONArray();

            try
            {
                postSubmitModel.put("Title", post.getTitle());
                postSubmitModel.put("Description", post.getDescription());
                postSubmitModel.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(post.getDate()));

                for (String image : imageIDs)
                {
                    images.put(image);
                }

                for (String activity : activityIDs)
                {
                    activities.put(activity);
                }

                postSubmitModel.put("Images", images);
                postSubmitModel.put("Activities", activities);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            initialRequest = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_POST), postSubmitModel, postUpload, errorListener)
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + JWToken);
                    return headers;
                }
            };
        }

        apiHandler.addToRequestQueue(initialRequest);

        return result;
    }

    private Response.Listener<JSONObject> activityUpload = null;
    private Response.Listener<String> imageUpload = null;
    private Response.Listener<JSONObject> postUpload = null;

    public LiveData<DataSourceResponse<List<Item>>> searchUsers(String query, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("query", query);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_USER, params), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Follow>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Follow>>>(){}.getType());

                if (apiResponse.isSuccessful())
                {
                    List<Item> items = new ArrayList<>(apiResponse.getResponse());
                    result.setValue(new DataSourceResponse<>(items));
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

        return result;
    }

    public LiveData<DataSourceResponse<List<Follow>>> getFollowees(String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Follow>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_FOLLOWEES), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Follow>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Follow>>>(){}.getType());

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
                result.setValue(new DataSourceResponse<>(error.toString()));
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

        return result;
    }

    public LiveData<DataSourceResponse<List<Follow>>> getFollowers(String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Follow>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_FOLLOWERS), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                ApiResponse<List<Follow>> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<List<Follow>>>(){}.getType());

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
                result.setValue(new DataSourceResponse<>(error.toString()));
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

        return result;
    }

    public LiveData<DataSourceResponse<List<String>>> getCities(String country)
    {
        MutableLiveData<DataSourceResponse<List<String>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JSONObject body = new JSONObject();
        try
        {
            body.put("country", country);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://countriesnow.space/api/v0.1/countries/cities", body, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                CityResponseModel apiResponse = new Gson().fromJson(response.toString(), new TypeToken<CityResponseModel>(){}.getType());

                if (!apiResponse.isError())
                {
                    result.setValue(new DataSourceResponse<>(apiResponse.getData()));
                }
                else
                {
                    result.setValue(new DataSourceResponse<>(apiResponse.getMsg()));
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                result.setValue(new DataSourceResponse<>(error.toString()));
            }
        });


        apiHandler.addToRequestQueue(request);

        return result;
    }

    public LiveData<DataSourceResponse<List<Activity>>> getPostActivities(int postID, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Activity>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("postID", String.valueOf(postID));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_POST_ACTIVITIES, params), null, new Response.Listener<JSONObject>()
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
                result.setValue(new DataSourceResponse<>(error.toString()));
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

        return result;
    }
}
