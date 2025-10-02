package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.api.client.AuthApi;
import ge.tbc.testautomation.data.model.request.AuthRequest;
import ge.tbc.testautomation.data.model.response.AuthResponse;

public class AuthSteps {

    private final AuthApi authApi = new AuthApi();

    public String generateToken(String username, String password) {
        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);
        AuthResponse response = authApi.getToken(request);
        return response.getToken();
    }
}
