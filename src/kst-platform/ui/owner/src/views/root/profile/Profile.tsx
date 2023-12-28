import { Box, Button, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { FormattedMessage } from "react-intl";
import { useRouteLoaderData } from "react-router-dom";
import PageHeader from "../../../components/header/PageHeader";
import ProfileForm from "../../../components/profile-form/ProfileForm";
import useAuthContext from "../../../hooks/useAuthContext";
import LOCALES from "../../../i18n/locales";
import { TRootLoaderData } from "../RootView";
import DataField from "../../../components/data-field/DataField";

const Profile = () => {
  const { keycloakService } = useAuthContext();
  const { profile } = useRouteLoaderData("root") as TRootLoaderData;
  const [locale, setLocale] = useState<string>(
    profile?.preferredLanguage ?? LOCALES.ENGLISH
  );

  useEffect(() => {
    if (!profile) {
      return;
    }
    const { preferredLanguage } = profile;
    if (preferredLanguage) {
      setLocale(preferredLanguage);
    }
  }, [profile]);

  return (
    <Stack spacing={3}>
      <PageHeader>
        <FormattedMessage id="my_profile" />
      </PageHeader>
      <Typography component="h2" variant="h5">
          <FormattedMessage id="profile_properties" />
        </Typography>
      <Stack spacing={1}>
        <DataField
          name="user-id"
          label={<FormattedMessage id="user_id" />}
          value={profile?.idpProfile?.userId}
        />
        <DataField
          name="email"
          label={<FormattedMessage id="email" />}
          value={profile?.idpProfile?.email}
        />
        <DataField
          name="firstname"
          label={<FormattedMessage id="firstname" />}
          value={profile?.idpProfile?.firstname}
        />
        <DataField
          name="lastname"
          label={<FormattedMessage id="lastname" />}
          value={profile?.idpProfile?.lastname}
        />
      </Stack>
      <Stack spacing={2}>
        <Typography gutterBottom={true}>
          <em><FormattedMessage id="profile_properties_change" /></em>
        </Typography>
        <Box>
          <Button variant="contained" href={keycloakService.accountConsoleUrl}>
            <FormattedMessage id="edit_personal_information" />
          </Button>
        </Box>
      </Stack>
      <Stack spacing={1}>
        <Typography component="h2" variant="h5">
          <FormattedMessage id="profile_settings" />
        </Typography>
          <ProfileForm
            id={profile?.id}
            preferedLanguage={locale}
            onPreferedLanguageChange={setLocale}
            showDeleteButton={true}
          />
      </Stack>
    </Stack>
  );
};

export default Profile;
