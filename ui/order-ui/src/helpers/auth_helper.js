import { UserManager } from 'oidc-client';

const settings = {
  authority: `http://${process.env.REACT_APP_KEYCLOAK_URL}/realms/my_realm/`,
  client_id: "frontend-client",
  redirect_uri: `http://${process.env.REACT_APP_LOGIN_REDIRECT_URL}/signin-callback.html`,
  post_logout_redirect_uri: `http://${process.env.REACT_APP_LOGIN_REDIRECT_URL}`,
  response_type: 'code',
  scope: "openid profile message.read",
};

const userManager = new UserManager(settings);

export const getAuthorization = () => {
    return userManager.getUser();
}

export const removeAuthorization = () => {
    return userManager.removeUser();
}

export const login = () => {
    return userManager.signinRedirect();
}

export const logout = () => {
    return userManager.signoutRedirect();
}
