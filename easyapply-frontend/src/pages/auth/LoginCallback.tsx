import React, { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';

const LoginCallback: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    console.log('Current URL:', window.location.href);
    const error = searchParams.get('error');

    if (error) {
      console.log('Error from backend:', error);
      navigate('/login', {
        replace: true,
        state: { error: decodeURIComponent(error) },
      });
      return;
    }

    // Verify session via cookie
    fetch('http://localhost:8090/api/check-session', {
      method: 'GET',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(res => {
        if (res.ok) {
          return res.json();
        }
        throw new Error('Session check failed');
      })
      .then(data => {
        console.log('Session data:', data);
        localStorage.setItem('role', data.role);
        localStorage.setItem('userId', data.id);
        localStorage.setItem('email', data.email);
        navigate('/dashboard/candidat/jobs', { replace: true });
      })
      .catch(err => {
        console.error('Session check error:', err);
        navigate('/login', {
          replace: true,
          state: { error: 'Échec de l\'authentification LinkedIn' },
        });
      });
  }, [searchParams, navigate]);

  return (
    <div className="container mx-auto p-4 text-center">
      <p>Redirection en cours...</p>
    </div>
  );
};

export default LoginCallback;