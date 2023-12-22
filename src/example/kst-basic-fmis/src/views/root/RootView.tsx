import { Box } from "@mui/material";
import { Container } from "@mui/system";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { deDE, enUS } from "@mui/x-date-pickers/locales";
import dayjs from "dayjs";
import "dayjs/locale/de";
import "dayjs/locale/en";
import customParseFormat from "dayjs/plugin/customParseFormat";
import {
  ConsumerConsentRequestDTOStatusEnum,
  ProsumerConsentApiInterface,
  ProsumerConsentDTO,
  ProsumerConsentDTOStatusEnum,
  ProsumerConsentRequestDTO,
  ProsumerCreateConsentRequestDTO,
  ProsumerCreateConsentRequestDTOPurposeEnum,
  UpdateConsentRequestDTOStatusEnum,
} from "kst-api";
import { useCallback, useEffect, useMemo, useState } from "react";
import { IntlProvider } from "react-intl";
import {
  LoaderFunction,
  Outlet,
  useLoaderData,
  useRevalidator,
  useSearchParams,
} from "react-router-dom";
import APIFactory from "../../api/APIFactory";
import Footer from "../../components/footer/Footer";
import Navigation from "../../components/navigation/Navigation";
import useAppConfig from "../../hooks/useAppConfig";
import useAuthContext from "../../hooks/useAuthContext";
import LOCALES from "../../i18n/locales";
import messages from "../../i18n/messages";
import AppConsentDataItemTypes from "../../model/AppConsentDataItemTypes";
import KeycloakService from "../../services/KeycloakService";
import { isSetEqual } from "../../utils/ArrayUtils";
import ConsentDialog from "./ConsentDialog";
import NoAccessDialog from "./NoAccessDialog";
import useStyles from "../../hooks/useStyles";

dayjs.extend(customParseFormat);

const LOCALE_SEARCH_PARAM = "locale";

export type TRootLoaderData = {
  loaded: boolean;
  activeConsent?: ProsumerConsentDTO;
  isConsentLevelFull?: boolean;
};

const DefaultRootData: TRootLoaderData = {
  loaded: false,
};

const createConsentRequest = async (
  prosumerConsentApi: ProsumerConsentApiInterface,
  consumedDataItemTypes: Array<string>,
  providedDataItemTypes: Array<string>
): Promise<ProsumerConsentRequestDTO> => {
  const ownerId = KeycloakService.getInstance().getUserId() ?? "";
  const createConsentRequestDto: ProsumerCreateConsentRequestDTO = {
    ownerId,
    consumedDataItemTypes: new Set<string>(consumedDataItemTypes),
    providedDataItemTypes: new Set<string>(providedDataItemTypes),
    dataUsageStatement: "We want to offer you a FMIS.",
    purpose: ProsumerCreateConsentRequestDTOPurposeEnum.Service,
  };
  const createdConsentRequest = await prosumerConsentApi.createConsentRequest1(
    createConsentRequestDto
  );
  return createdConsentRequest;
};

const getOrCreateConsentRequest = async (
  prosumerConsentApi: ProsumerConsentApiInterface,
  consentType: string
) => {
  const dataItemTypes = AppConsentDataItemTypes[consentType] ?? [];
  const { consumedDataItemTypes, providedDataItemTypes } = dataItemTypes;
  const consentRequests =
    await prosumerConsentApi.getAllRequestedConsentRequests1(
      ConsumerConsentRequestDTOStatusEnum.Pending
    );
  if (consentRequests.length > 0) {
    const consentRequest = consentRequests[0];
    const {
      id,
      consumedDataItemTypes: consentConsumedDataItemTypes,
      providedDataItemTypes: consentProvidedDataItemTypes,
    } = consentRequest;
    const consumedDataItemTypeSet = new Set(consumedDataItemTypes);
    const providedDataItemTypeSet = new Set(providedDataItemTypes);
    // Make shure that we actually have a set here, seems to be an array at runtime
    const consentConsumedDataItemSet = new Set(consentConsumedDataItemTypes);
    const consentProvidedDataItemSet = new Set(consentProvidedDataItemTypes);
    if (
      isSetEqual(consumedDataItemTypeSet, consentConsumedDataItemSet) &&
      isSetEqual(providedDataItemTypeSet, consentProvidedDataItemSet)
    ) {
      return consentRequest;
    }
    await prosumerConsentApi.updateRequestedConsentRequest1(id, {
      id,
      status: UpdateConsentRequestDTOStatusEnum.Retracted,
    });
  }
  const newConsentRequest = await createConsentRequest(
    prosumerConsentApi,
    consumedDataItemTypes,
    providedDataItemTypes
  );
  return newConsentRequest;
};

const isFullLevelConsent = (consent: ProsumerConsentDTO) => {
  const dataItemTypes = AppConsentDataItemTypes["full"] ?? [];
  const { consumedDataItemTypes, providedDataItemTypes } = dataItemTypes;
  return (
    isSetEqual(
      new Set(consumedDataItemTypes),
      new Set(consent.consumedDataItemTypes)
    ) &&
    isSetEqual(
      new Set(providedDataItemTypes),
      new Set(consent.providedDataItemTypes)
    )
  );
};

export const rootLoader: LoaderFunction = async ({
  request,
}): Promise<TRootLoaderData> => {
  const keycloakService = KeycloakService.getInstance();
  if (!keycloakService.authenticated) {
    return DefaultRootData;
  }
  try {
    const { signal } = request;
    const prosumerConsentApi = await APIFactory.createProsumerConsentApi();
    const consents = await prosumerConsentApi.getAllConsents1(
      ProsumerConsentDTOStatusEnum.Active,
      { signal }
    );
    const activeConsent = consents.length > 0 ? consents[0] : undefined;
    const isConsentLevelFull =
      activeConsent && isFullLevelConsent(activeConsent);

    return {
      loaded: true,
      activeConsent,
      isConsentLevelFull,
    };
  } catch (e) {
    return { loaded: true };
  }
};

const styleCreator = () => ({
  outletContainerStyle: {
    flex: 1,
    marginTop: "0.3em",
    marginBottom: "0.3em",
  },
});

const RootView = () => {
  const revalidator = useRevalidator();
  const { loaded, activeConsent } = useLoaderData() as TRootLoaderData;
  const [searchParams, setSearchParams] = useSearchParams();
  const { appConfig } = useAppConfig();
  const { keycloakService } = useAuthContext();
  const [locale, setLocale] = useState<string>(() =>
    navigator.language === LOCALES.GERMAN ? LOCALES.GERMAN : LOCALES.ENGLISH
  );
  const styles = useStyles(styleCreator);

  const [isConsentDialogOpen, setIsConsentDialogOpen] =
    useState<boolean>(false);

  const adapterLocale = useMemo(() => {
    switch (locale) {
      case LOCALES.GERMAN:
        return "de";
      default:
        return "en";
    }
  }, [locale]);

  const localeText = useMemo(() => {
    switch (locale) {
      case LOCALES.GERMAN:
        return deDE.components.MuiLocalizationProvider.defaultProps.localeText;
      default:
        return enUS.components.MuiLocalizationProvider.defaultProps.localeText;
    }
  }, [locale]);

  const isOwnerAccount = useMemo(
    () => keycloakService.hasOwnerRole(),
    [keycloakService]
  );

  const onLocaleChange = useCallback(
    (locale: string) => {
      if (!locale) {
        return;
      }
      setSearchParams((prevParams) => {
        const urlSearchParams = new URLSearchParams(prevParams);
        urlSearchParams.set(LOCALE_SEARCH_PARAM, locale);
        return urlSearchParams;
      });
    },
    [setSearchParams]
  );

  const handleSignoutClicked = useCallback(() => {
    keycloakService.logout();
  }, [keycloakService]);

  const handleConsent = useCallback(
    (consentType: string) => {
      const redirectToOwnerUI = async () => {
        const prosumerConsentApi = await APIFactory.createProsumerConsentApi();
        const consentRequest = await getOrCreateConsentRequest(
          prosumerConsentApi,
          consentType
        );
        const { id } = consentRequest;
        const kstOwnerUI = appConfig?.kstOwnerUI;
        const consentRequestUrl = new URL(`consent-request/${id}`, kstOwnerUI);
        consentRequestUrl.searchParams.set(
          "redirect_uri",
          window.location.href
        );
        window.location.assign(consentRequestUrl);
      };
      redirectToOwnerUI();
    },
    [appConfig?.kstOwnerUI]
  );

  useEffect(() => {
    const paramLocale = searchParams.get(LOCALE_SEARCH_PARAM);
    if (paramLocale) {
      setLocale(paramLocale);
    }
  }, [searchParams]);

  useEffect(() => {
    if (loaded && !activeConsent) {
      setIsConsentDialogOpen(true);
    }
  }, [activeConsent, loaded]);

  useEffect(() => {
    if (!keycloakService.authenticated || loaded) {
      return;
    }
    if (revalidator.state === "idle") {
      revalidator.revalidate();
    }
  }, [keycloakService.authenticated, loaded, revalidator]);

  return (
    <IntlProvider
      messages={messages[locale]}
      locale={locale}
      defaultLocale={LOCALES.DEFAULT}
    >
      <LocalizationProvider
        dateAdapter={AdapterDayjs}
        adapterLocale={adapterLocale}
        localeText={localeText}
      >
        <Box display="flex" flexDirection="column" height="100%">
          <Navigation locale={locale} onLocaleChange={onLocaleChange} />
          <Container
            component="main"
            maxWidth="lg"
            sx={styles.outletContainerStyle}
          >
            <Outlet />
          </Container>
          <Footer />
        </Box>
        <NoAccessDialog
          open={keycloakService.authenticated && !isOwnerAccount}
          onSignoutClick={handleSignoutClicked}
        />
        <ConsentDialog
          open={isConsentDialogOpen}
          onClose={handleSignoutClicked}
          onContinue={handleConsent}
        />
      </LocalizationProvider>
    </IntlProvider>
  );
};

export default RootView;
