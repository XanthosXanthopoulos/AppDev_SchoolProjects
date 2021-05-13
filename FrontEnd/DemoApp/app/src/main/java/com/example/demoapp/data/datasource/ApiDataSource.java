package com.example.demoapp.data.datasource;

import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.demoapp.data.model.Activity;
import com.example.demoapp.data.model.ContentType;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Deque;
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

    public LiveData<DataSourceResponse<List<Item>>> search(SearchQueryModel query, String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.SEARCH), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                List<Item> searchResults = new ArrayList<>();
                try
                {
                    if (response.getBoolean("successful"))
                    {
                        JSONArray items = response.getJSONArray("response");
                        Gson gson = new Gson();

                        for (int i = 0; i < items.length(); ++i)
                        {
                            ContentType contentType = gson.fromJson(items.getJSONObject(i).get("contentType").toString(), ContentType.class);

                            switch (contentType)
                            {
                                case ACTIVITY:
                                    Activity activity = gson.fromJson(items.getJSONObject(i).toString(), Activity.class);
                                    searchResults.add(activity);
                                    break;
                                case IMAGE:
                                    break;
                                case TRIP:
                                    break;
                                default:
                            }
                        }
                    }
                    else
                    {
                        result.setValue(new DataSourceResponse<>(response.getString("errorMessage")));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    result.setValue(new DataSourceResponse<>("Response error"));
                }

                result.setValue(new DataSourceResponse<>(searchResults));
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

    public LiveData<DataSourceResponse<Bitmap>> requestImage(String imageID, String JWToken)
    {
        MutableLiveData<DataSourceResponse<Bitmap>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("id", imageID);


        ImageRequest request = new ImageRequest(ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD, params), new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                result.setValue(new DataSourceResponse<>(response));
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.err.println(error.networkResponse);
                result.setValue(new DataSourceResponse<>("Network Error"));
            }
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

    public LiveData<DataSourceResponse<List<Item>>> getFeed(String JWToken)
    {
        MutableLiveData<DataSourceResponse<List<Item>>> result = new MutableLiveData<>();
        ApiHandler apiHandler = ApiHandler.getInstance();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiRoutes.getRoute(ApiRoutes.Route.FEED), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if (!response.getBoolean("successful"))
                    {
                        result.setValue(new DataSourceResponse<>(response.getString("errorMessage")));
                        return;
                    }

                    List<Item> feed = new ArrayList<>();

                    JSONArray items = response.getJSONArray("response");
                    Gson gson = new Gson();

                    for (int i = 0; i < items.length(); ++i)
                    {
                        feed.add(gson.fromJson(items.getJSONObject(i).toString(), Post.class));
                    }

                    result.setValue(new DataSourceResponse<>(feed));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    result.setValue(new DataSourceResponse<>("Deserialization error."));
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
        });

        apiHandler.addToRequestQueue(request);


        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<DataSourceResponse<Boolean>> uploadPost(Post post, String JWToken)
    {
        MutableLiveData<DataSourceResponse<Boolean>> result = new MutableLiveData<>();
        JSONObject postBody = new JSONObject();
        ApiHandler apiHandler = ApiHandler.getInstance();
        ImageLoader imageLoader = ImageLoader.getInstance();

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
                JsonObjectRequest request;

                if (pendingActivities.isEmpty())
                {
                    request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD), null, postUpload, errorListener);
                }
                else
                {
                    request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD), null, activityUpload, errorListener);
                }

                apiHandler.addToRequestQueue(request);
            }
        };

        imageUpload = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                ImageUploadRequest request;
                imageIDs.add(response);

                if (pendingImages.isEmpty())
                {
                    //request = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.IMAGE_DOWNLOAD), null, activityUpload, errorListener);
                    result.setValue(new DataSourceResponse<>(true));
                    return;
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
            initialRequest = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_ACTIVITY), activitySubmitModel, activityUpload, errorListener);
        }
        else
        {
            JSONObject postSubmitModel = new JSONObject();
            try
            {
                postSubmitModel.put("Title", post.getTitle());
                postSubmitModel.put("Description", post.getDescription());
                postSubmitModel.put("Date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(post.getDate()));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            initialRequest = new JsonObjectRequest(Request.Method.POST, ApiRoutes.getRoute(ApiRoutes.Route.UPLOAD_POST), postSubmitModel, postUpload, errorListener);
        }

        apiHandler.addToRequestQueue(initialRequest);

        return result;
    }

    private Response.Listener<JSONObject> activityUpload = null;
    private Response.Listener<String> imageUpload = null;
    private Response.Listener<JSONObject> postUpload = null;
}
