import { Box, Dialog, DialogContent, DialogTitle } from "@mui/material";
import { Container } from "@mui/system";
import { OwnerProfileDTO, ResponseError } from "owner-api";
import { useCallback, useEffect, useMemo, useState } from "react";
import { FormattedMessage, IntlProvider } from "react-intl";
import {
  ActionFunctionArgs,
  LoaderFunction,
  Outlet,
  useLoaderData,
  useRevalidator,
} from "react-router-dom";
import APIFactory from "../../api/APIFactory";
import Footer from "../../components/footer/Footer";
import Navigation from "../../components/navigation/Navigation";
import ProfileForm from "../../components/profile-form/ProfileForm";
import useAuthContext from "../../hooks/useAuthContext";
import LOCALES from "../../i18n/locales";
import messages from "../../i18n/messages";
import KeycloakService from "../../services/KeycloakService";
import NoAccessDialog from "./NoAccessDialog";
import ProfileCacheService from "../../services/ProfileCacheService";
import Sidebar from "../../components/sidebar/Sidebar";

export type TRootLoaderData = {
  loaded: boolean;
  profile?: OwnerProfileDTO;
};

const DefaultRootData: TRootLoaderData = {
  loaded: false,
};

export const rootLoader: LoaderFunction = async ({
  request,
}): Promise<TRootLoaderData> => {
  if (!KeycloakService.getInstance().authenticated) {
    return DefaultRootData;
  }
  try {
    const profile = await ProfileCacheService.getInstance().getProfile(request);
    return { loaded: true, profile };
  } catch (e) {
    if (e instanceof ResponseError) {
      const { response } = e;
      // upon first login the profile does not exist
      if (response.status !== 404) {
        throw response;
      }
    }
    return { loaded: true };
  }
};

export const rootAction = async (
  args: ActionFunctionArgs
): Promise<TRootLoaderData> => {
  const { request } = args;
  const formData = await request.formData();
  const id = formData.get("id")?.toString();
  const preferredLanguage =
    formData.get("preferedLanguage")?.toString() ?? LOCALES.DEFAULT;
  const ownerApi = await APIFactory.createOwnerApi();
  if (!id) {
    const profile = await ownerApi.createMyProfile(
      { preferredLanguage },
      { signal: request.signal }
    );
    ProfileCacheService.getInstance().invalidate();
    return { loaded: true, profile };
  }
  const profile = await ownerApi.updateMyProfile(
    id,
    { preferredLanguage },
    { signal: request.signal }
  );
  ProfileCacheService.getInstance().invalidate();
  return { loaded: true, profile };
};

const RootView = () => {
  const revalidator = useRevalidator();
  const { profile, loaded } = useLoaderData() as TRootLoaderData;
  const { keycloakService } = useAuthContext();
  const [locale, setLocale] = useState<string>(() =>
    navigator.language === LOCALES.GERMAN ? LOCALES.GERMAN : LOCALES.ENGLISH
  );

  const isAuthenticated = useMemo(
    () => keycloakService.authenticated,
    [keycloakService]
  );
  const isOwnerAccount = useMemo(
    () => keycloakService.hasOwnerRole(),
    [keycloakService]
  );

  const handleSignoutClicked = useCallback(() => {
    keycloakService.logout();
  }, [keycloakService]);

  useEffect(() => {
    if (
      keycloakService.authenticated &&
      !loaded &&
      revalidator.state === "idle"
    ) {
      revalidator.revalidate();
    }
  }, [revalidator, keycloakService.authenticated, loaded]);

  useEffect(() => {
    if (!profile) {
      return;
    }
    const { preferredLanguage } = profile;
    if (preferredLanguage && preferredLanguage !== locale) {
      setLocale(preferredLanguage);
    }
  }, [locale, profile]);

  return (
    <IntlProvider
      messages={messages[locale]}
      locale={locale}
      defaultLocale={LOCALES.DEFAULT}
    >
      <Box display="flex" flexDirection="column" height="100%">
        <Navigation />
        <Box display="flex" flex={1}>
          {isAuthenticated && <Sidebar />}
          <Container component="main" maxWidth={false} sx={{ flex: 5, marginTop: 1, marginBottom: 1 }}>
            <Outlet />
          </Container>
        </Box>
        <Footer />
      </Box>
      <Dialog maxWidth="md" open={isOwnerAccount && !profile && loaded}>
        <DialogTitle>
          <FormattedMessage id="setup_profile" />
        </DialogTitle>
        <DialogContent>
          <ProfileForm
            id={profile?.id}
            preferedLanguage={locale}
            onPreferedLanguageChange={setLocale}
            fullWidth={true}
          />
        </DialogContent>
      </Dialog>
      <NoAccessDialog
        open={keycloakService.authenticated && !isOwnerAccount}
        onSignoutClick={handleSignoutClicked}
      />
    </IntlProvider>
  );
};

export default RootView;
