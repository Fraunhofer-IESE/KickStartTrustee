import Keycloak, { KeycloakInitOptions } from "keycloak-js";
import BearerTokenMiddleware from "../api/BearerTokenMiddleware";

export type TKeycloakConfig = {
  url: string;
  realm: string;
  clientId: string;
};

export const KeycloakProviderInitConfig: KeycloakInitOptions = {
  onLoad: "check-sso",
  pkceMethod: "S256",
  silentCheckSsoRedirectUri: window.location.origin + "/silent-check-sso.html",
};

class KeycloakService {
  private static instance: KeycloakService;

  private _keycloakConfig?: TKeycloakConfig;
  private _keycloak?: Keycloak;

  private constructor() {}

  public static getInstance(): KeycloakService {
    if (!KeycloakService.instance) {
      KeycloakService.instance = new KeycloakService();
    }
    return KeycloakService.instance;
  }

  /* Properties */
  public get authenticated(): boolean {
    return Boolean(this._keycloak?.authenticated);
  }

  public get token(): string | undefined {
    return this._keycloak?.token;
  }

  public get isTokenExpired(): boolean {
    if (!this._keycloak?.authenticated) {
      return false;
    }
    return this._keycloak.isTokenExpired();
  }

  public get accountConsoleUrl(): string | undefined {
    if (!this.authenticated) {
      return undefined;
    }
    return this._keycloak?.createAccountUrl({
      redirectUri: window.location.href,
    });
  }

  /* Methods */

  private _setTokenIfChanged() {
    const tokenMiddleware = BearerTokenMiddleware.getInstance();
    if (tokenMiddleware.token !== this.token) {
      tokenMiddleware.token = this.token;
    }
  }

  private static _redirecToLogin() {
    window.location.href = "/login";
  }

  public async init(
    keycloakConfig: TKeycloakConfig,
    options: KeycloakInitOptions
  ): Promise<void> {
    if (!this._keycloak || this._keycloakConfig !== keycloakConfig) {
      this._keycloakConfig = keycloakConfig;
      this._keycloak = new Keycloak(keycloakConfig);
    }
    const initialized = await this._keycloak.init(options);
    if (initialized) {
      this._setTokenIfChanged();
    }
    this._keycloak.onAuthLogout = KeycloakService._redirecToLogin;
  }

  public async updateToken(minValidity: number): Promise<boolean> {
    try {
      if (!this._keycloak) {
        return false;
      }
      const refreshed = await this._keycloak.updateToken(minValidity);
      if (refreshed) {
        this._setTokenIfChanged();
      }
      return refreshed;
    } catch (e) {
      this.logout();
      return false;
    }
  }

  public register(): void {
    if (!this._keycloak || this.authenticated) {
      return;
    }
    this._keycloak.register({ redirectUri: window.location.href });
  }

  public login(): void {
    if (!this._keycloak || this.authenticated) {
      return;
    }
    this._keycloak.login({ redirectUri: window.location.href });
  }

  public logout(): void {
    if (!this._keycloak?.authenticated) {
      return;
    }
    this._keycloak.logout({
      redirectUri: window.location.origin,
    });
  }

  public getUserId(): string | undefined {
    return this._keycloak?.tokenParsed?.sub;
  }

  public getUserName(): string | undefined {
    return this._keycloak?.tokenParsed?.preferred_username;
  }

  public getGivenName(): string | undefined {
    return this._keycloak?.tokenParsed?.given_name;
  }

  public getFamilyName(): string | undefined {
    return this._keycloak?.tokenParsed?.family_name;
  }

  public hasOwnerRole(): boolean {
    return this._keycloak?.hasRealmRole("OWNER") ?? false;
  }
}

export default KeycloakService;
