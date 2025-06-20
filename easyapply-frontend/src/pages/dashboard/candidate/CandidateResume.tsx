import React, { useState, useEffect } from 'react';
import ResumePreview from '../../../components/resume/ResumePreview';
import Card from '../../../components/ui/Card';
import Button from '../../../components/ui/Button';
import Input from '../../../components/ui/Input';
import { useNavigate } from 'react-router-dom';
import type { Resume } from '../../../types';


const CandidateResume: React.FC = () => {
  const [isEditing, setIsEditing] = useState(false);
  const [resume, setResume] = useState<Resume>({
    id: '',
    userId: '',
    fullName: '',
    jobTitle: '',
    email: '',
    location: '',
    experience: [],
    education: [],
    skills: [],
    languages: [],
  });
  const navigate = useNavigate();

  // Fetch resume data from backend using the logged-in candidate's ID
  useEffect(() => {
    const fetchResume = async () => {
      try {
        const token = localStorage.getItem('token');
        const candidateId = getCandidateIdFromAuth(); // Implement this function
        const response = await fetch(`http://localhost:8090/api/candidat/profile/${candidateId}`, {
          method: 'GET',
          credentials: 'include',
          headers: { 
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json' },
        });

        if (!response.ok) {
          throw new Error('Erreur lors de la récupération du profil');
        }

        const data = await response.json();
        setResume({
          id: String(data.id || ''),
          userId: String(data.candidatId || data.id || ''), // Adjust based on API response
          fullName: data.nom || '', // Map nom to fullName
          jobTitle: data.titreProfessionnel || '',
          email: data.email || '',
          location: data.telephone || '', // Assuming location maps to telephone
          experience: data.experiences?.map((exp: any) => ({
            poste: exp.poste,
            company: typeof exp.entreprise === 'string' ? exp.entreprise : exp.entreprise?.nom || '',
            location: exp.lieu || exp.location || '',
            startDate: exp.dateDebut,
            endDate: exp.dateFin || '',
            description: exp.description || '',
          })) || [],
          education: data.formations?.map((edu: any) => ({
            institution: edu.etablissement || '',
            degree: edu.diplome || '',
            field: edu.field || '',
            startDate: edu.dateDebut || '',
            endDate: edu.dateFin || '',
          })) || [],
          skills: data.competences?.map((comp: any) => comp.nom || '') || [],
          languages: data.langues?.map((lang: any) => ({
            name: lang.nom || '',
            level: lang.niveau || '',
          })) || [],
        });
      } catch (err) {
        console.error('Fetch resume error:', err);
      }
    };

    fetchResume();
  }, []);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
  };

  const handleSave = async () => {
    try {
     // Implement this function
      const token = localStorage.getItem('token');
    
      const response = await fetch(`http://localhost:8090/api/candidat/profile`, {
      
        method: 'POST',
        credentials: 'include',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json' },
        body: JSON.stringify({
         
          nom: resume.fullName, // Map fullName to nom
          jobTitle: resume.jobTitle,
          email: resume.email,
          telephone: resume.location, // Mapping location to telephone
          experiences: resume.experience?.map(exp => ({
            poste: exp.title,
            entreprise: exp.company,
            lieu: exp.location,
            dateDebut: exp.startDate,
            dateFin: exp.endDate,
            description: exp.description,
          })),
          formations: resume.education?.map(edu => ({
            etablissement: edu.institution,
            diplome: edu.degree,
            field: edu.field,
            dateDebut: edu.startDate,
            dateFin: edu.endDate,
          })),
          competences: resume.skills?.map(skill => ({ nom: skill })),
          langues: resume.languages?.map(lang => ({ nom: lang.name, niveau: lang.level })),
        }),
      });

      if (!response.ok) {
        throw new Error('Erreur lors de l\'enregistrement du profil');
      }

      const data = await response.json();
      console.log('Profile saved:', data);
      setIsEditing(false);
      navigate('/dashboard/candidat/jobs'); // Redirect after saving
    } catch (err) {
      console.error('Save error:', err);
      alert('Une erreur est survenue lors de l\'enregistrement.');
    }
  };

  // Functions to add new items
  const addExperience = () => {
    setResume(prev => ({
      ...prev,
      experience: [...prev.experience, { title: '', company: '', location: '', startDate: '', endDate: '', description: '' }],
    }));
  };

  const addEducation = () => {
    setResume(prev => ({
      ...prev,
      education: [...prev.education, { institution: '', degree: '', field: '', startDate: '', endDate: '' }],
    }));
  };

  const addSkill = () => {
    setResume(prev => ({
      ...prev,
      skills: [...prev.skills, ''],
    }));
  };

  const addLanguage = () => {
    setResume(prev => ({
      ...prev,
      languages: [...prev.languages, { name: '', level: '' }],
    }));
  };

  return (
    <div>
      {!isEditing ? (
        <ResumePreview resume={resume} onEdit={handleEdit} />
      ) : (
        <div className="max-w-3xl mx-auto">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-semibold">Modifier mon CV</h2>
            <div className="flex space-x-3">
              <Button variant="secondary" size="sm" onClick={handleCancel}>
                Annuler
              </Button>
              <Button variant="primary" size="sm" onClick={handleSave}>
                Enregistrer
              </Button>
            </div>
          </div>

          <Card className="mb-8">
            <h3 className="text-lg font-semibold mb-4">Informations personnelles</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                label="Nom complet"
                id="fullName"
                name="fullName"
                value={resume.fullName || ''}
                onChange={(e) => setResume({ ...resume, fullName: e.target.value })}
              />
              <Input
                label="Titre professionnel"
                id="jobTitle"
                name="jobTitle"
                value={resume.jobTitle || ''}
                onChange={(e) => setResume({ ...resume, jobTitle: e.target.value })}
              />
              <Input
                label="Email"
                id="email"
                name="email"
                type="email"
                value={resume.email || ''}
                onChange={(e) => setResume({ ...resume, email: e.target.value })}
              />
              <Input
                label="Localisation"
                id="location"
                name="location"
                value={resume.location || ''}
                onChange={(e) => setResume({ ...resume, location: e.target.value })}
              />
            </div>
          </Card>

          <Card className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold">Expérience professionnelle</h3>
              <Button size="sm" variant="secondary" onClick={addExperience}>
                Ajouter
              </Button>
            </div>
            {resume.experience?.map((exp, index) => (
              <div key={index} className="mb-6 pb-6 border-b border-gray-200 last:border-0 last:pb-0 last:mb-0">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    label="Titre du poste"
                    id={`exp-title-${index}`}
                    name={`exp-title-${index}`}
                    value={exp.title || ''}
                    onChange={(e) => {
                      const newExperience = [...resume.experience!];
                      newExperience[index] = { ...newExperience[index], title: e.target.value };
                      setResume({ ...resume, experience: newExperience });
                    }}
                  />
                  <Input
                    label="Entreprise"
                    id={`exp-company-${index}`}
                    name={`exp-company-${index}`}
                    value={exp.company || ''}
                    onChange={(e) => {
                      const newExperience = [...resume.experience!];
                      newExperience[index] = { ...newExperience[index], company: e.target.value };
                      setResume({ ...resume, experience: newExperience });
                    }}
                  />
                  <Input
                    label="Localisation"
                    id={`exp-location-${index}`}
                    name={`exp-location-${index}`}
                    value={exp.location || ''}
                    onChange={(e) => {
                      const newExperience = [...resume.experience!];
                      newExperience[index] = { ...newExperience[index], location: e.target.value };
                      setResume({ ...resume, experience: newExperience });
                    }}
                  />
                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Date de début"
                      id={`exp-start-${index}`}
                      name={`exp-start-${index}`}
                      type="date"
                      value={exp.startDate || ''}
                      onChange={(e) => {
                        const newExperience = [...resume.experience!];
                        newExperience[index] = { ...newExperience[index], startDate: e.target.value };
                        setResume({ ...resume, experience: newExperience });
                      }}
                    />
                    <Input
                      label="Date de fin"
                      id={`exp-end-${index}`}
                      name={`exp-end-${index}`}
                      type="date"
                      value={exp.endDate || ''}
                      onChange={(e) => {
                        const newExperience = [...resume.experience!];
                        newExperience[index] = { ...newExperience[index], endDate: e.target.value };
                        setResume({ ...resume, experience: newExperience });
                      }}
                      placeholder="Present"
                    />
                  </div>
                </div>
                <div className="mt-4">
                  <label htmlFor={`exp-description-${index}`} className="block text-sm font-medium text-gray-700">
                    Description
                  </label>
                  <textarea
                    id={`exp-description-${index}`}
                    name={`exp-description-${index}`}
                    rows={3}
                    className="w-full p-2 border rounded"
                    value={exp.description || ''}
                    onChange={(e) => {
                      const newExperience = [...resume.experience!];
                      newExperience[index] = { ...newExperience[index], description: e.target.value };
                      setResume({ ...resume, experience: newExperience });
                    }}
                  />
                </div>
              </div>
            ))}
          </Card>

          <Card className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold">Formation</h3>
              <Button size="sm" variant="secondary" onClick={addEducation}>
                Ajouter
              </Button>
            </div>
            {resume.education?.map((edu, index) => (
              <div key={index} className="mb-6 pb-6 border-b border-gray-200 last:border-0 last:pb-0 last:mb-0">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    label="Institution"
                    id={`edu-institution-${index}`}
                    name={`edu-institution-${index}`}
                    value={edu.institution || ''}
                    onChange={(e) => {
                      const newEducation = [...resume.education!];
                      newEducation[index] = { ...newEducation[index], institution: e.target.value };
                      setResume({ ...resume, education: newEducation });
                    }}
                  />
                  <Input
                    label="Diplôme"
                    id={`edu-degree-${index}`}
                    name={`edu-degree-${index}`}
                    value={edu.degree || ''}
                    onChange={(e) => {
                      const newEducation = [...resume.education!];
                      newEducation[index] = { ...newEducation[index], degree: e.target.value };
                      setResume({ ...resume, education: newEducation });
                    }}
                  />
                  <Input
                    label="Domaine d'études"
                    id={`edu-field-${index}`}
                    name={`edu-field-${index}`}
                    value={edu.field || ''}
                    onChange={(e) => {
                      const newEducation = [...resume.education!];
                      newEducation[index] = { ...newEducation[index], field: e.target.value };
                      setResume({ ...resume, education: newEducation });
                    }}
                  />
                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Date de début"
                      id={`edu-start-${index}`}
                      name={`edu-start-${index}`}
                      type="date"
                      value={edu.startDate || ''}
                      onChange={(e) => {
                        const newEducation = [...resume.education!];
                        newEducation[index] = { ...newEducation[index], startDate: e.target.value };
                        setResume({ ...resume, education: newEducation });
                      }}
                    />
                    <Input
                      label="Date de fin"
                      id={`edu-end-${index}`}
                      name={`edu-end-${index}`}
                      type="date"
                      value={edu.endDate || ''}
                      onChange={(e) => {
                        const newEducation = [...resume.education!];
                        newEducation[index] = { ...newEducation[index], endDate: e.target.value };
                        setResume({ ...resume, education: newEducation });
                      }}
                    />
                  </div>
                </div>
              </div>
            ))}
          </Card>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <Card>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Compétences</h3>
                <Button size="sm" variant="secondary" onClick={addSkill}>
                  Ajouter
                </Button>
              </div>
              <div className="space-y-3">
                {resume.skills?.map((skill, index) => (
                  <div key={index} className="flex items-center">
                    <Input
                      id={`skill-${index}`}
                      name={`skill-${index}`}
                      value={skill || ''}
                      onChange={(e) => {
                        const newSkills = [...resume.skills!];
                        newSkills[index] = e.target.value;
                        setResume({ ...resume, skills: newSkills });
                      }}
                      wrapperClassName="flex-1 mb-0"
                    />
                    <button
                      className="ml-2 text-gray-500 hover:text-error-500"
                      aria-label="Supprimer"
                      onClick={() => {
                        const newSkills = resume.skills!.filter((_, i) => i !== index);
                        setResume({ ...resume, skills: newSkills });
                      }}
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        viewBox="0 0 20 20"
                        fill="currentColor"
                        className="w-5 h-5"
                      >
                        <path
                          fillRule="evenodd"
                          d="M8.75 1A2.75 2.75 0 006 3.75v.443c-.795.077-1.584.176-2.365.298a.75.75 0 10.23 1.482l.149-.022.841 10.518A2.75 2.75 0 007.596 19h4.807a2.75 2.75 0 002.742-2.53l.841-10.52.149.023a.75.75 0 00.23-1.482A41.03 41.03 0 0014 4.193V3.75A2.75 2.75 0 0011.25 1h-2.5zM10 4c.84 0 1.673.025 2.5.075V3.75c0-.69-.56-1.25-1.25-1.25h-2.5c-.69 0-1.25.56-1.25 1.25v.325C8.327 4.025 9.16 4 10 4zM8.58 7.72a.75.75 0 00-1.5.06l.3 7.5a.75.75 0 101.5-.06l-.3-7.5zm4.34.06a.75.75 0 10-1.5-.06l-.3 7.5a.75.75 0 101.5.06l.3-7.5z"
                          clipRule="evenodd"
                        />
                      </svg>
                    </button>
                  </div>
                ))}
              </div>
            </Card>

            <Card>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Langues</h3>
                <Button size="sm" variant="secondary" onClick={addLanguage}>
                  Ajouter
                </Button>
              </div>
              <div className="space-y-3">
                {resume.languages?.map((lang, index) => (
                  <div key={index} className="grid grid-cols-2 gap-2 items-center">
                    <Input
                      id={`lang-name-${index}`}
                      name={`lang-name-${index}`}
                      value={lang.name || ''}
                      onChange={(e) => {
                        const newLanguages = [...resume.languages!];
                        newLanguages[index] = { ...newLanguages[index], name: e.target.value };
                        setResume({ ...resume, languages: newLanguages });
                      }}
                      wrapperClassName="mb-0"
                    />
                    <div className="flex items-center">
                      <Input
                        id={`lang-level-${index}`}
                        name={`lang-level-${index}`}
                        value={lang.level || ''}
                        onChange={(e) => {
                          const newLanguages = [...resume.languages!];
                          newLanguages[index] = { ...newLanguages[index], level: e.target.value };
                          setResume({ ...resume, languages: newLanguages });
                        }}
                        wrapperClassName="flex-1 mb-0"
                      />
                      <button
                        className="ml-2 text-gray-500 hover:text-error-500"
                        aria-label="Supprimer"
                        onClick={() => {
                          const newLanguages = resume.languages!.filter((_, i) => i !== index);
                          setResume({ ...resume, languages: newLanguages });
                        }}
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          viewBox="0 0 20 20"
                          fill="currentColor"
                          className="w-5 h-5"
                        >
                          <path
                            fillRule="evenodd"
                            d="M8.75 1A2.75 2.75 0 006 3.75v.443c-.795.077-1.584.176-2.365.298a.75.75 0 10.23 1.482l.149-.022.841 10.518A2.75 2.75 0 007.596 19h4.807a2.75 2.75 0 002.742-2.53l.841-10.52.149.023a.75.75 0 00.23-1.482A41.03 41.03 0 0014 4.193V3.75A2.75 2.75 0 0011.25 1h-2.5zM10 4c.84 0 1.673.025 2.5.075V3.75c0-.69-.56-1.25-1.25-1.25h-2.5c-.69 0-1.25.56-1.25 1.25v.325C8.327 4.025 9.16 4 10 4zM8.58 7.72a.75.75 0 00-1.5.06l.3 7.5a.75.75 0 101.5-.06l-.3-7.5zm4.34.06a.75.75 0 10-1.5-.06l-.3 7.5a.75.75 0 101.5.06l.3-7.5z"
                            clipRule="evenodd"
                          />
                        </svg>
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </Card>
          </div>

          <div className="mt-8 flex justify-end space-x-4">
            <Button variant="secondary" onClick={handleCancel}>
              Annuler
            </Button>
            <Button onClick={handleSave}>
              Enregistrer les modifications
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

const getCandidateIdFromAuth = () => {
  const userId = localStorage.getItem('userId');
  const role = localStorage.getItem('role');

  if (!userId || role !== 'CANDIDAT') {
    throw new Error("Utilisateur non autorisé ou identifiant manquant.");
  }

  return userId;
};

export default CandidateResume;