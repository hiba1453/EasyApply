import React, { useEffect ,useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

const LoginCallback: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    console.log('Current URL:', window.location.href);
    const error = searchParams.get('error');
    const [userInfo, setUserInfo] = useState<{id?: string, role?: string, email?: string}>({});

   
    const email = searchParams.get('email');


    if (error) {
      console.log('Error from backend:', error);
      navigate('/login', { 
        replace: true,
        state: { error: decodeURIComponent(error) }
      });
      return;
    }

    // Verify session by checking the cookie
    fetch('http://localhost:8090/api/check-session', {
      method: 'GET',
      credentials: 'include', // Send cookies
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(res => {
        if (res.ok) {
          console.log('Session check successful', res);
          return res.json();
        }
        throw new Error('Session check failed');
      })
      .then(data => {
        console.log('Session data:', data);
        // Store role and userId for frontend use
        localStorage.setItem('role', data.role);
        localStorage.setItem('userId', data.userId);
        console.log('Role and userId stored in localStorage:', data.role, data.id);
        // Already at /dashboard/candidat/jobs, no need to navigate
        // If not, navigate: navigate('/dashboard/candidat/jobs', { replace: true });
      })
      .catch(err => {
        console.error('Session check error:', err);
        navigate('/login', { 
          replace: true,
          state: { error: 'Échec de l\'authentification LinkedIn' }
        });
      });
  }, [navigate, searchParams]);

  return (
    <div className="container mx-auto p-4 text-center">
      <p>Redirection en cours...</p>
    </div>
  );
};

export default LoginCallback;