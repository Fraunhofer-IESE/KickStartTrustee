import { APP_BASE_PATH } from "../App";

export type TAppConfig = {
  keycloakBaseUrl: string;
  keycloakRealm: string;
  keycloakClientId: string;
  kstApiBasePath: string;
  kstOwnerUI: string;
};

class AppConfig {
  private static instance: AppConfig;
  private _config?: TAppConfig;

  private constructor() {}

  static getInstance(): AppConfig {
    if (!AppConfig.instance) {
      AppConfig.instance = new AppConfig();
    }
    return AppConfig.instance;
  }

  public async getConfig(): Promise<TAppConfig> {
    if (!this._config) {
      const response = await fetch(`${APP_BASE_PATH}/config.json`);
      const newConfig: TAppConfig = await response.json();
      this._config = newConfig;
    }
    return this._config;
  }
}

export default AppConfig;
