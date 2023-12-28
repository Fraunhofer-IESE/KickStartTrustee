import {
  CatalogDataitemApi,
  CatalogDataitemApiInterface,
  Configuration,
  OwnerApi,
  OwnerApiInterface,
  OwnerAuditApi,
  OwnerAuditApiInterface,
  OwnerConsentApi,
  OwnerConsentApiInterface,
  OwnerDataApi,
  OwnerDataApiInterface,
} from "owner-api";
import AppConfig from "../config/AppConfig";
import BearerTokenMiddleware from "./BearerTokenMiddleware";

class APIFactory {
  private static OWNER_API = "OwnerApi";
  private static OWNER_AUDIT_API = "OwnerAuditApi";
  private static OWNER_CONSENT_API = "OwnerConsentApi";
  private static OWNER_DATA_API = "OwnerDataApi";
  private static DATA_CATALOG_API = "DataCatalogApi";
  private static _apiCache = new Map();

  private static async createApiConfiguration(): Promise<Configuration> {
    const config = await AppConfig.getInstance().getConfig();
    const basePath = config.kstApiBasePath;
    const bearerTokenMiddleware = BearerTokenMiddleware.getInstance();
    const configuration = new Configuration({
      basePath,
      middleware: [bearerTokenMiddleware],
    });
    return configuration;
  }

  static async createOwnerApi(): Promise<OwnerApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.OWNER_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new OwnerApi(configuration);
      APIFactory._apiCache.set(APIFactory.OWNER_API, api);
      return api;
    }
    return cachedApi;
  }

  static async createConsentApi(): Promise<OwnerConsentApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.OWNER_CONSENT_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new OwnerConsentApi(configuration);
      APIFactory._apiCache.set(APIFactory.OWNER_CONSENT_API, api);
      return api;
    }
    return cachedApi;
  }

  static async createAuditApi(): Promise<OwnerAuditApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.OWNER_AUDIT_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new OwnerAuditApi(configuration);
      APIFactory._apiCache.set(APIFactory.OWNER_AUDIT_API, api);
      return api;
    }
    return cachedApi;
  }

  static async createDataApi(): Promise<OwnerDataApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.OWNER_DATA_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new OwnerDataApi(configuration);
      APIFactory._apiCache.set(APIFactory.OWNER_DATA_API, api);
      return api;
    }
    return cachedApi;
  }

  static async createCatalogApi(): Promise<CatalogDataitemApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.DATA_CATALOG_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new CatalogDataitemApi(configuration);
      APIFactory._apiCache.set(APIFactory.DATA_CATALOG_API, api);
      return api;
    }
    return cachedApi;
  }
}

export default APIFactory;
