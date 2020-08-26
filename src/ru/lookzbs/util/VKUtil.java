package ru.lookzbs.util;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.secure.TokenChecked;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import com.vk.api.sdk.queries.secure.SecureCheckTokenQuery;
import ru.lookzbs.Core;

import java.util.Properties;

public class VKUtil {

    public static VkApiClient vk;
    private static TransportClient transportClient;
    private ServiceActor credentials;

    private static String groupAccessToken;
    public static String secretKey;

    public static int groupId;

    public static GroupActor groupActor;

    public VKUtil() {
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        Properties props = Core.loadConfig();

        groupAccessToken = props.getProperty("vkgroupaccesstoken");
        groupId = Integer.parseInt(props.getProperty("vkgroupid"));
        secretKey = props.getProperty("vksecretkey");

        int vkAppId = Integer.parseInt(props.getProperty("vkappid"));
        String vkClientSecret = props.getProperty("vkclientsecret");
        String vkServiceToken = props.getProperty("vkservicetoken");

        credentials = new ServiceActor(vkAppId, vkClientSecret, vkServiceToken);
        groupActor = new GroupActor(groupId, groupAccessToken);
    }

    public MessagesSendQuery message() {
        return vk.messages().send(groupActor);
    }

    //Returns user_id after checkToken
    //If user_id = 0 it means token is invalid
    public int checkToken(String token) {
        int user_id = 0;
        try {
            SecureCheckTokenQuery sctq = vk.secure().checkToken(credentials).token(token);
            TokenChecked tc = sctq.execute();

            if (tc.getSuccess().getValue().equals("1")) {
                user_id = tc.getUserId();
            }
        } catch (ApiException e) {
            //invalid token
        } catch (ClientException e) {
            //hz che
        }
        return user_id;
    }
}
