import { Navigate } from "react-router-dom";
import { defaultFilterQueryParams } from "../consent-request/ConsentRequestListView";

const Home = () => {
  return <Navigate to={`/consent-request?${defaultFilterQueryParams}`} />;
};

export default Home;
