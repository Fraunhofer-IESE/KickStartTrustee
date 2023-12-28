import React from "react";
import KeycloakService from "../services/KeycloakService";

export type TAuthContext = {
  initialized: boolean;
  keycloakService: KeycloakService;
};

export const AuthContext = React.createContext<TAuthContext>({
  initialized: false,
  keycloakService: KeycloakService.getInstance()
});
