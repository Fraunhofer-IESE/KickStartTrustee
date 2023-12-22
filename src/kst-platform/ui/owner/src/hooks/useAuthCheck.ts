import { redirect } from "react-router-dom";
import KeycloakService from "../services/KeycloakService";

export const authCheck = async (): Promise<null> => {
  if (!KeycloakService.getInstance().authenticated) {
    throw redirect("/login");
  }
  return null;
};

const useAuthCheck = () => {
    return authCheck;
}

export default useAuthCheck;
