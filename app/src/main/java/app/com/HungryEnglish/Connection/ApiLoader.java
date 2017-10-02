package app.com.HungryEnglish.Connection;

/**
 * Created by good on 9/14/2017.
 */

public class ApiLoader {

//    public <T> void getResponse(Context context, HashMap<String, String> hashMap, String url, Class<?> genericClass, final CallBack callBack) {
//        final Class<?> cls = genericClass;
//        final Context ctx = context;
//        Call<String> call = ApiHandler.getApiService().getPost(hashMap, url);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                String error = "";
//                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//                String jsonRes = "";
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body());
//                    if (jsonObject.has("authentication")) {
//                        if (jsonObject.optBoolean("authentication")) {
//                            AppUtils.logout(ctx);
//                        }
//                    }
//                    jsonRes = String.valueOf(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (callBack != null) {
//                    Object object = gson.fromJson(jsonRes, cls);
//                    callBack.onResponse(call, response, error, object);
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                if (callBack != null) {
//                    if (t instanceof SocketTimeoutException)
//                        callBack.onSocketTimeout(call, t);
//                    else
//                        callBack.onFail(call, t);
//                }
//            }
//        });
//    }
}
