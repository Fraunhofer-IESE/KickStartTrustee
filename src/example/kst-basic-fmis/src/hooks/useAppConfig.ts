import { useEffect, useState } from "react";
import AppConfig, { TAppConfig } from "../config/AppConfig";

const useAppConfig = (): {
  appConfig?: TAppConfig;
  isError: boolean;
  isLoading: boolean;
} => {
  const [appConfig, setAppConfig] = useState<TAppConfig>();
  const [isError, setIsError] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  useEffect(() => {
    const loadConfig = async () => {
      try {
        setIsLoading(true);
        const appConfigInstance = AppConfig.getInstance()
        const newAppConfig = await appConfigInstance.getConfig();
        setAppConfig(newAppConfig);
      } catch (e) {
        setIsError(true);
      } finally {
        setIsLoading(false);
      }
    };
    loadConfig();
  }, []);

  return { appConfig, isError, isLoading };
};

export default useAppConfig;
