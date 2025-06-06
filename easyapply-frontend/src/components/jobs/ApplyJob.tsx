import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Card from '../ui/Card';
import Button from '../ui/Button';

const ApplyJob: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [cvFile, setCvFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch('http://localhost:8090/api/check-session', {
          method: 'GET',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        });

        console.log('Session check status:', response.status);
        console.log('Session check headers:', Object.fromEntries(response.headers.entries()));

        if (!response.ok) {
          let errorData: any = {};
          try {
            errorData = await response.json();
            console.log('Session check error body:', errorData);
          } catch (e) {
            console.error('Failed to parse session check error:', e);
          }
          throw new Error(errorData.error || `Erreur HTTP ${response.status}`);
        }

        const data = await response.json();
        console.log('Session check success body:', data);
        localStorage.setItem('role', data.role);
        localStorage.setItem('userId', data.id);
        localStorage.setItem('email', data.email);
      } catch (err) {
        if (err instanceof Error) {
          console.error('Session check error:', err.message);
        } else {
          console.error('Session check error:', String(err));
        }
        localStorage.removeItem('role');
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        navigate('/login', { state: { error: 'Veuillez vous connecter pour postuler.' } });
      }
    };

    checkSession();
  }, [navigate]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setCvFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    setLoading(true);

    try {
      const formData = new FormData();
      if (cvFile) {
        formData.append('cv', cvFile);
      }

      const response = await fetch(`http://localhost:8090/offre/${id}/apply`, {
        method: 'POST',
        credentials: 'include',
        body: formData,
      });

      console.log('Apply response status:', response.status);
      console.log('Apply response headers:', Object.fromEntries(response.headers.entries()));

      if (!response.ok) {
        let errorData: any = {};
        try {
          errorData = await response.json();
          console.log('Apply error body:', errorData);
        } catch (e) {
          console.error('Failed to parse apply error:', e);
        }
        throw new Error(errorData.error || `Erreur HTTP ${response.status}`);
      }

      const result = await response.json();
      console.log('Apply success body:', result);
      setSuccess(result.message || 'Candidature soumise avec succès !');
      setTimeout(() => {
        navigate('/dashboard/candidat/jobs');
      }, 2000);
    } catch (err) {
      if (err instanceof Error) {
        console.error('Apply submit error:', err.message);
        setError(err.message || 'Une erreur est survenue.');
      } else {
        console.error('Apply submit error:', err);
        setError('Une erreur est survenue.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mx-auto p-8">
      <Card className="max-w-md mx-auto">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Postuler à l’offre</h2>
        {error && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 p-3 bg-green-100 text-green-700 rounded">
            {success}
          </div>
        )}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="cv" className="block text-sm font-medium text-gray-700">
              Télécharger votre CV (optionnel)
            </label>
            <input
              type="file"
              id="cv"
              accept=".pdf,.doc,.docx"
              onChange={handleFileChange}
              className="mt-1 block w-full p-2 border rounded"
            />
          </div>
          <div className="flex gap-4">
            <Link to={`/dashboard/candidat/jobs/${id}`}>
              <Button variant="secondary" disabled={loading}>
                Annuler
              </Button>
            </Link>
            <Button type="submit" variant="primary" disabled={loading}>
              {loading ? 'Envoi...' : 'Postuler'}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
};

export default ApplyJob;