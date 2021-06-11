package com.example.demoapp.data.datasource;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.demoapp.data.Event;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.Country;
import com.example.demoapp.data.model.Follow;
import com.example.demoapp.data.model.Item;
import com.example.demoapp.data.model.Place;
import com.example.demoapp.data.model.Post;
import com.example.demoapp.data.model.User;
import com.example.demoapp.data.model.api.request.RegisterCredentialsModel;
import com.example.demoapp.data.model.api.request.SearchQueryModel;
import com.example.demoapp.data.model.api.request.SingInCredentialsModel;
import com.example.demoapp.data.model.api.response.ApiResponse;
import com.example.demoapp.data.model.api.response.AuthenticationResponseModel;
import com.example.demoapp.data.model.api.response.CityResponseModel;
import com.example.demoapp.data.model.api.response.ProfileInfoResponseModel;
import com.example.demoapp.data.model.datasource.DataSourceResponse;
import com.example.demoapp.util.ApiHandler;
import com.example.demoapp.util.ApiRoutes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.LOGIN), postBody, response ->
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
            }, error ->
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network error"));
            });

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception ignored)
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

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.REGISTER), postBody, response ->
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
            }, error ->
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network error"));
            });

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception ignored)
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
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.PROFILE_INFO), null, response ->
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
            }, error ->
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network error"));
            })
            {
                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + JWToken);
                    return headers;
                }
            };

            apiHandler.addToRequestQueue(request);
        }
        catch (Exception ignored)
        {

        }

        return result;
    }

    public LiveData<DataSourceResponse<AuthenticationResponseModel>> updateProfile(ProfileInfoResponseModel profile, String JWToken)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();
        JSONObject postBody = new JSONObject();
        MutableLiveData<DataSourceResponse<AuthenticationResponseModel>> result = new MutableLiveData<>();

        Response.ErrorListener errorListener = error -> result.setValue(new DataSourceResponse<>(error.toString()));

        imageUpload = response ->
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
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getResponse()));
                    }
                    else
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                    }
                }
            }, errorListener)
            {
                @Override
                public Map<String, String> getHeaders()
                {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + JWToken);
                    return headers;
                }
            };


            apiHandler.addToRequestQueue(request);
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

                initialRequest = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPDATE_PROFILE), postBody, response ->
                {
                    ApiResponse<AuthenticationResponseModel> apiResponse = new Gson().fromJson(response.toString(), new TypeToken<ApiResponse<AuthenticationResponseModel>>(){}.getType());

                    if (apiResponse.isSuccessful())
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getResponse()));
                    }
                    else
                    {
                        result.setValue(new DataSourceResponse<>(apiResponse.getErrorMessage()));
                    }
                }, error ->
                {
                    System.err.println(error.networkResponse);
                    result.setValue(new DataSourceResponse<>("Network error"));
                })
                {
                    @Override
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };
            }
            catch (Exception ignored)
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

        if (query.getLatitude() != 0)
        {
            params.put("latitude", String.valueOf(query.getLatitude()));
            params.put("longtitude", String.valueOf(query.getLongitude()));
            params.put("radius", String.valueOf(query.getRadius()));
        }

        Response.Listener<JSONObject> searchResponse = response ->
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
        };

        Response.ErrorListener errorListener = error ->
        {
            System.err.println(error.networkResponse);
            result.setValue(new DataSourceResponse<>("Network error"));
        };

        if (query.getCity() != null && !query.getCity().isEmpty() && query.getLatitude() == 0)
        {
            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "GEO_API_KEY");
            geoParams.put("q", query.getCity());
            JsonObjectRequest init = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response ->
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
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };

                apiHandler.addToRequestQueue(request);
            }, error ->
            {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, searchResponse, errorListener)
                {
                    @Override
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };

                apiHandler.addToRequestQueue(request);
            });

            apiHandler.addToRequestQueue(init);
        }
        else
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, searchResponse, errorListener)
            {
                @Override
                public Map<String, String> getHeaders()
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

    public LiveData<DataSourceResponse<List<Activity>>> searchNearActivities(double latitude, double longtitude, double radius, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Activity>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", String.valueOf(latitude));
        params.put("longtitude", String.valueOf(longtitude));
        params.put("radius", String.valueOf(radius));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_ACTIVITY, params), null, response ->
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
        }, error -> result.setValue(new DataSourceResponse<>(error.getMessage())))
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }
        };

        apiHandler.addToRequestQueue(request);

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

        if (query.getLatitude() != 0)
        {
            params.put("latitude", String.valueOf(query.getLatitude()));
            params.put("longtitude", String.valueOf(query.getLongitude()));
            params.put("radius", String.valueOf(query.getRadius()));
        }

        Response.Listener<JSONObject> searchResponse = response ->
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
        };

        Response.ErrorListener errorListener = error ->
        {
            System.err.println(error.networkResponse);
            result.setValue(new DataSourceResponse<>("Network error"));
        };

        if (query.getCity() != null && !query.getCity().isEmpty() && query.getLatitude() == 0)
        {
            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "GEO_API_KEY");
            geoParams.put("q", query.getCity());
            JsonObjectRequest init = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response ->
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
            }, error ->
            {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_POST, params), null, searchResponse, errorListener)
                {
                    @Override
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };

                apiHandler.addToRequestQueue(request);
            });

            apiHandler.addToRequestQueue(init);
        }
        else
        {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_POST, params), null, searchResponse, errorListener)
            {
                @Override
                public Map<String, String> getHeaders()
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

    public LiveData<DataSourceResponse<List<Item>>> getFeed(boolean self, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("self", String.valueOf(self));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.FEED, params), null, response ->
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
        }, error ->
        {
            System.err.println(error.networkResponse);
            result.setValue(new DataSourceResponse<>("Network error"));
        })
        {
            @Override
            public Map<String, String> getHeaders()
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_POST, params), null, response ->
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
        }, error ->
        {
            System.err.println(error.networkResponse);
            result.setValue(new DataSourceResponse<>("Network error"));
        })
        {
            @Override
            public Map<String, String> getHeaders()
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

        Response.ErrorListener errorListener = error -> result.setValue(new DataSourceResponse<>(false));

        postUpload = response -> result.setValue(new DataSourceResponse<>(true));

        activityUpload = response ->
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
                    public Map<String, String> getHeaders()
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
                    activitySubmitModel.put("City", activity.getCity());
                    activitySubmitModel.put("Tags", activity.getTags());
                    activitySubmitModel.put("latitude", activity.getLatitude());
                    activitySubmitModel.put("longtitude", activity.getLongtitude());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                HashMap<String, String> geoParams = new HashMap<>();
                geoParams.put("key", "GEO_API_KEY");
                geoParams.put("q", activity.getAddress() + ", " + activity.getCity() + ", " + activity.getCountry().label);
                request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response12 ->
                {
                    try
                    {
                        if (activitySubmitModel.getDouble("latitude") == 0)
                        {
                            JSONObject coordinates = response12.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                            activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                            activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    JsonObjectRequest request14 = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                    {
                        @Override
                        public Map<String, String> getHeaders()
                        {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + JWToken);
                            return headers;
                        }
                    };

                    apiHandler.addToRequestQueue(request14);
                }, error ->
                {
                    JsonObjectRequest request13 = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                    {
                        @Override
                        public Map<String, String> getHeaders()
                        {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + JWToken);
                            return headers;
                        }
                    };

                    apiHandler.addToRequestQueue(request13);
                });
            }

            apiHandler.addToRequestQueue(request);
        };

        imageUpload = response ->
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
                        public Map<String, String> getHeaders()
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
                        activitySubmitModel.put("City", activity.getCity());
                        activitySubmitModel.put("Tags", activity.getTags());
                        activitySubmitModel.put("latitude", activity.getLatitude());
                        activitySubmitModel.put("longtitude", activity.getLongtitude());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    HashMap<String, String> geoParams = new HashMap<>();
                    geoParams.put("key", "GEO_API_KEY");
                    geoParams.put("q", activity.getAddress() + ", " + activity.getCity() + ", " + activity.getCountry().label);
                    request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response1 ->
                    {
                        try
                        {
                            if (activitySubmitModel.getDouble("latitude") == 0)
                            {
                                JSONObject coordinates = response1.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                                activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                                activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        JsonObjectRequest request12 = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                        {
                            @Override
                            public Map<String, String> getHeaders()
                            {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + JWToken);
                                return headers;
                            }
                        };

                        apiHandler.addToRequestQueue(request12);
                    }, error ->
                    {
                        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                        {
                            @Override
                            public Map<String, String> getHeaders()
                            {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Bearer " + JWToken);
                                return headers;
                            }
                        };

                        apiHandler.addToRequestQueue(request1);
                    });
                }
            }
            else
            {
                request = new ImageUploadRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_UPLOAD), pendingImages.pop(), JWToken, imageUpload, errorListener);
            }

            apiHandler.addToRequestQueue(request);
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
                activitySubmitModel.put("City", activity.getCity());
                activitySubmitModel.put("Country", activity.getCountry().code);
                activitySubmitModel.put("Tags", activity.getTags());
                activitySubmitModel.put("latitude", activity.getLatitude());
                activitySubmitModel.put("longtitude", activity.getLongtitude());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            HashMap<String, String> geoParams = new HashMap<>();
            geoParams.put("key", "GEO_API_KEY");
            geoParams.put("q", activity.getAddress() + ", " + activity.getCity() + ", " + activity.getCountry().label);
            initialRequest = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response ->
            {
                try
                {
                    if (activitySubmitModel.getDouble("latitude") == 0)
                    {
                        JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                        activitySubmitModel.put("Latitude", coordinates.getDouble("lat"));
                        activitySubmitModel.put("Longtitude", coordinates.getDouble("lng"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                {
                    @Override
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };

                apiHandler.addToRequestQueue(request);
            }, error ->
            {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener)
                {
                    @Override
                    public Map<String, String> getHeaders()
                    {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + JWToken);
                        return headers;
                    }
                };

                apiHandler.addToRequestQueue(request);
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
                public Map<String, String> getHeaders()
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH_USER, params), null, response ->
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
        }, error ->
        {
            System.err.println(error.networkResponse);
            result.setValue(new DataSourceResponse<>("Network error"));
        })
        {
            @Override
            public Map<String, String> getHeaders()
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_FOLLOWEES), null, response ->
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
        }, error -> result.setValue(new DataSourceResponse<>(error.toString())))
        {
            @Override
            public Map<String, String> getHeaders()
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_FOLLOWERS), null, response ->
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
        }, error -> result.setValue(new DataSourceResponse<>(error.toString())))
        {
            @Override
            public Map<String, String> getHeaders()
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://countriesnow.space/api/v0.1/countries/cities", body, response ->
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
        }, error -> result.setValue(new DataSourceResponse<>(error.toString())));


        apiHandler.addToRequestQueue(request);

        return result;
    }

    public LiveData<DataSourceResponse<List<Activity>>> getPostActivities(int postID, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Activity>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("postID", String.valueOf(postID));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.GET_POST_ACTIVITIES, params), null, response ->
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
        }, error -> result.setValue(new DataSourceResponse<>(error.toString())))
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }
        };

        apiHandler.addToRequestQueue(request);

        return result;
    }

    public LiveData<DataSourceResponse<Place>> getLocationInfo(double latitude, double longitude)
    {
        MutableLiveData<DataSourceResponse<Place>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> geoParams = new HashMap<>();
        geoParams.put("key", "GEO_API_KEY");
        geoParams.put("q", latitude + "+" + longitude);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response ->
        {
            Place place = new Place();

            try
            {
                JSONObject info = response.getJSONArray("results").getJSONObject(0).getJSONObject("components");
                place.setCountry(info.getString("country"));

                if (info.has("city")) place.setCity(info.getString("city"));
                else if (info.has("neighbourhood")) place.setCity(info.getString("neighbourhood"));

                if (info.has("road") && info.has("house_number"))
                {
                    place.setAddress(info.getString("road") + " " + info.getString("house_number"));
                }
                else if (info.has("road")) place.setAddress(info.getString("road"));

                place.setLatitude(latitude);
                place.setLongitude(longitude);

                result.setValue(new DataSourceResponse<>(place));
            }
            catch (JSONException e)
            {
                e.printStackTrace();

                result.setValue(new DataSourceResponse<>("Network error"));
            }
        }, error -> result.setValue(new DataSourceResponse<>("Network error")));

        apiHandler.addToRequestQueue(request);

        return result;
    }

    public LiveData<DataSourceResponse<Place>> getCoordinates(String query)
    {
        MutableLiveData<DataSourceResponse<Place>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> geoParams = new HashMap<>();
        geoParams.put("key", "GEO_API_KEY");
        geoParams.put("q", query);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getGeoRoute(geoParams), null, response ->
        {
            Place place = new Place();
            try
            {
                JSONObject coordinates = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry");
                place.setLatitude(coordinates.getDouble("lat"));
                place.setLongitude(coordinates.getDouble("lng"));
                result.setValue(new DataSourceResponse<>(place));
            }
            catch (JSONException e)
            {
                e.printStackTrace();

                result.setValue(new DataSourceResponse<>("Place not found"));
            }
        }, error -> result.setValue(new DataSourceResponse<>("Network error")));

        apiHandler.addToRequestQueue(request);

        return result;
    }

    public void logout(String JWToken)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();

        StringRequest request = new StringRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.LOGOUT), response ->
        {

        }, error ->
        {

        })
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }
        };

        apiHandler.addToRequestQueue(request);
    }

    public void deletePost(int postID, String JWToken)
    {
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("postID", String.valueOf(postID));
        StringRequest request = new StringRequest(Request.Method.DELETE, ApiRoutes.getRoute(ApiRoutes.Route.DELETE_POST, params), response ->
        {

        }, error ->
        {

        })
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + JWToken);
                return headers;
            }
        };

        apiHandler.addToRequestQueue(request);
    }
}
