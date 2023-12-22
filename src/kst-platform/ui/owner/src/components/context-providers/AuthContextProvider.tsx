import { type KeycloakInitOptions } from "keycloak-js";
import { PropsWithChildren, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import useAppConfig from "../../hooks/useAppConfig";
import KeycloakService, {
  TKeycloakConfig,
} from "../../services/KeycloakService";

type AuthContextProviderProps = {
  initOptions: KeycloakInitOptions;
  keycloakService: KeycloakService;
  LoadingComponent: JSX.Element;
};

const AuthContextProvider = (
  props: PropsWithChildren<AuthContextProviderProps>
) => {
  const { children, initOptions, keycloakService, LoadingComponent } = props;
  const { appConfig, isLoading: isConfigLoading } = useAppConfig();
  const [loading, setLoading] = useState<boolean>(isConfigLoading);
  const [initialized, setInitialized] = useState<boolean>(false);

  useEffect(() => {
    setLoading((prevLoading) => prevLoading || isConfigLoading);
  }, [isConfigLoading]);

  useEffect(() => {
    const initKeycloak = async () => {
      if (!appConfig || isConfigLoading) {
        return;
      }
      setLoading(true);
      try {
        const { keycloakBaseUrl, keycloakRealm, keycloakClientId } = appConfig;
        const keycloakConfig: TKeycloakConfig = {
          url: keycloakBaseUrl,
          realm: keycloakRealm,
          clientId: keycloakClientId,
        };
        await keycloakService.init(keycloakConfig, initOptions);
        setInitialized(true);
      } finally {
        setLoading(false);
      }
    };
    initKeycloak();
  }, [appConfig, initOptions, isConfigLoading, keycloakService]);

  useEffect(() => {
    if (!initialized) {
      return;
    }
    const interval = setInterval(() => {
      keycloakService.updateToken(10);
    }, 10 * 1000);
    return () => clearInterval(interval);
  }, [initialized, keycloakService]);

  if (!initialized || loading) {
    return LoadingComponent;
  }

  return (
    <AuthContext.Provider value={{ initialized, keycloakService }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContextProvider;
