import { useContext } from 'react';
import { AuthContext, TAuthContext } from '../contexts/AuthContext';

const useAuthContext = (): TAuthContext => {
  return useContext(AuthContext);
};

export default useAuthContext;
