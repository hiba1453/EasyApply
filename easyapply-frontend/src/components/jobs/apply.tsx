import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { CheckCircle, Upload } from 'lucide-react';
import Card from '../ui/Card';
import Button from '../ui/Button';
import { JobPosting } from '../../types';

const ApplyPage: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // Job ID from URL
  const navigate = useNavigate();

  // Mock job data (replace with actual data from route state or API)
  const job: JobPosting = {
    id: id || '',
    title: 'Développeur',
    company: 'RABAT',
    location: 'Rabat',
    salary: '$9',
    description: 'Description du poste de développeur.',
    tags: ['Développement'],
    postedDate: new Date().toISOString(),
  };

  // Form state
  const [coverLetter, setCoverLetter] = useState('');
  const [cvFile, setCvFile] = useState<File | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  // Handle file upload
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setCvFile(e.target.files[0]);
    }
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    const token = localStorage.getItem('token'); // Assume token is stored here
    if (!token) {
      alert('Veuillez vous connecter pour postuler.');
      setIsSubmitting(false);
      return;
    }

    const formData = new FormData();
    formData.append('jobId', job.id);
    formData.append('coverLetter', coverLetter);
    if (cvFile) formData.append('cvFile', cvFile);

    try {
      const response = await fetch('http://localhost:8090/api/apply', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        setSuccess(true);
        setTimeout(() => navigate('/dashboard/candidate/applications'), 2000); // Redirect after 2s
      } else {
        const errorData = await response.json();
        alert(`Erreur: ${errorData.error || 'Échec de la candidature'}`);
      }
    } catch (error) {
      alert('Erreur lors de la soumission: ' + (error as Error).message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto p-4 max-w-3xl">
      <Card className="p-6">
        <h2 className="text-2xl font-bold mb-4">Postuler à l’offre : {job.title}</h2>
        <p className="text-gray-600 mb-6">
          Entreprise : {job.company} | Lieu : {job.location} | Salaire : {job.salary}
        </p>

        {success ? (
          <div className="text-center">
            <CheckCircle className="w-16 h-16 text-green-500 mx-auto mb-4" />
            <p className="text-lg text-green-600">Candidature soumise avec succès !</p>
            <p className="text-gray-500">Vous serez redirigé vers vos candidatures...</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} encType="multipart/form-data">
            <div className="mb-4">
              <label htmlFor="coverLetter" className="block text-sm font-medium text-gray-700 mb-1">
                Lettre de motivation
              </label>
              <textarea
                id="coverLetter"
                value={coverLetter}
                onChange={(e) => setCoverLetter(e.target.value)}
                className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                rows={4}
                placeholder="Décrivez pourquoi vous êtes un bon candidat..."
              />
            </div>

            <div className="mb-4">
              <label htmlFor="cvFile" className="block text-sm font-medium text-gray-700 mb-1">
                Télécharger votre CV
              </label>
              <div className="flex items-center">
                <label
                  htmlFor="cvFile"
                  className="flex items-center px-4 py-2 bg-gray-100 border border-gray-300 rounded-md cursor-pointer hover:bg-gray-200"
                >
                  <Upload className="w-5 h-5 mr-2" />
                  {cvFile ? cvFile.name : 'Choisir un fichier'}
                </label>
                <input
                  id="cvFile"
                  type="file"
                  accept=".pdf,.doc,.docx"
                  onChange={handleFileChange}
                  className="hidden"
                />
              </div>
            </div>

            <Button
              type="submit"
              variant="primary"
              size="lg"
              disabled={isSubmitting}
              className="w-full"
            >
              {isSubmitting ? 'Soumission en cours...' : 'Soumettre ma candidature'}
            </Button>
          </form>
        )}
      </Card>
    </div>
  );
};

export default ApplyPage;