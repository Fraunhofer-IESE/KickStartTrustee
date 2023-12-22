import {
  CatalogDataitemApi,
  CatalogDataitemApiInterface,
  Configuration,
  ProsumerConsentApi,
  ProsumerConsentApiInterface,
  ProsumerDataApi,
  ProsumerDataApiInterface,
} from "kst-api";
import AppConfig from "../config/AppConfig";
import BearerTokenMiddleware from "./BearerTokenMiddleware";

class APIFactory {
  private static DATA_CATALOG_API = "DataCatalogApi";
  private static PROSUMER_CONSENT_API = "ProsumerConsentApi";
  private static PROSUMER_DATA_API = "ProsumerDataApi";
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

  static async createProsumerConsentApi(): Promise<ProsumerConsentApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.PROSUMER_CONSENT_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new ProsumerConsentApi(configuration);
      APIFactory._apiCache.set(APIFactory.PROSUMER_CONSENT_API, api);
      return api;
    }
    return cachedApi;
  }

  static async createProsumerDataApi(): Promise<ProsumerDataApiInterface> {
    const cachedApi = APIFactory._apiCache.get(APIFactory.PROSUMER_DATA_API);
    if (!cachedApi) {
      const configuration = await APIFactory.createApiConfiguration();
      const api = new ProsumerDataApi(configuration);
      APIFactory._apiCache.set(APIFactory.PROSUMER_DATA_API, api);
      return api;
    }
    return cachedApi;
  }
}

export default APIFactory;
