import { useState, useContext, createContext, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole');
    const userData = localStorage.getItem('user');

    if (token && role && userData) {
      setIsAuthenticated(true);
      setUserRole(role);
      setUser(JSON.parse(userData));
    }

    setLoading(false);
  }, []);

  const login = async (email, password, role) => {
    const loginRes = await authService.login(email, password, role);
    setIsAuthenticated(true);
    setUserRole(loginRes.userRole);
    setUser(loginRes.user);
    return loginRes;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    setUserRole(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ user, userRole, isAuthenticated, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);