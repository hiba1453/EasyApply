import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Logout: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleLogout = async () => {
      try {
        await fetch('http://localhost:8090/api/logout', {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        });
        localStorage.removeItem('role');
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        navigate('/login', { replace: true });
      } catch (err) {
        console.error('Logout error:', err);
        navigate('/login', { replace: true });
      }
    };

    handleLogout();
  }, [navigate]);

  return <div>Logging out...</div>;
};

export default Logout;