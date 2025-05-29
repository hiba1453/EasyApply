import React, { useState } from 'react';
import ResumePreview from '../../../components/resume/ResumePreview';
import Card from '../../../components/ui/Card';
import Button from '../../../components/ui/Button';
import Input from '../../../components/ui/Input';
import { mockResume } from '../../../utils/mockData';

const CandidateResume: React.FC = () => {
  const [isEditing, setIsEditing] = useState(false);
  const [resume, setResume] = useState(mockResume);

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCancel = () => {
    setIsEditing(false);
  };

  const handleSave = () => {
    // Here you would save the resume data
    setIsEditing(false);
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
                defaultValue="Thomas Dubois"
              />
              <Input
                label="Titre professionnel"
                id="jobTitle"
                name="jobTitle"
                defaultValue="Développeur Frontend"
              />
              <Input
                label="Email"
                id="email"
                name="email"
                type="email"
                defaultValue="thomas@example.com"
              />
              <Input
                label="Localisation"
                id="location"
                name="location"
                defaultValue="Paris, France"
              />
            </div>
          </Card>

          <Card className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold">Expérience professionnelle</h3>
              <Button size="sm" variant="secondary">
                Ajouter
              </Button>
            </div>

            {resume.experience.map((exp, index) => (
              <div key={index} className="mb-6 pb-6 border-b border-gray-200 last:border-0 last:pb-0 last:mb-0">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    label="Titre du poste"
                    id={`exp-title-${index}`}
                    name={`exp-title-${index}`}
                    defaultValue={exp.title}
                  />
                  <Input
                    label="Entreprise"
                    id={`exp-company-${index}`}
                    name={`exp-company-${index}`}
                    defaultValue={exp.company}
                  />
                  <Input
                    label="Localisation"
                    id={`exp-location-${index}`}
                    name={`exp-location-${index}`}
                    defaultValue={exp.location}
                  />
                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Date de début"
                      id={`exp-start-${index}`}
                      name={`exp-start-${index}`}
                      defaultValue={exp.startDate}
                    />
                    <Input
                      label="Date de fin"
                      id={`exp-end-${index}`}
                      name={`exp-end-${index}`}
                      defaultValue={exp.endDate}
                      placeholder="Present"
                    />
                  </div>
                </div>
                <div className="mt-4">
                  <label htmlFor={`exp-description-${index}`} className="label">
                    Description
                  </label>
                  <textarea
                    id={`exp-description-${index}`}
                    name={`exp-description-${index}`}
                    rows={3}
                    className="input"
                    defaultValue={exp.description}
                  />
                </div>
              </div>
            ))}
          </Card>

          <Card className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold">Formation</h3>
              <Button size="sm" variant="secondary">
                Ajouter
              </Button>
            </div>

            {resume.education.map((edu, index) => (
              <div key={index} className="mb-6 pb-6 border-b border-gray-200 last:border-0 last:pb-0 last:mb-0">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    label="Institution"
                    id={`edu-institution-${index}`}
                    name={`edu-institution-${index}`}
                    defaultValue={edu.institution}
                  />
                  <Input
                    label="Diplôme"
                    id={`edu-degree-${index}`}
                    name={`edu-degree-${index}`}
                    defaultValue={edu.degree}
                  />
                  <Input
                    label="Domaine d'études"
                    id={`edu-field-${index}`}
                    name={`edu-field-${index}`}
                    defaultValue={edu.field}
                  />
                  <div className="grid grid-cols-2 gap-4">
                    <Input
                      label="Date de début"
                      id={`edu-start-${index}`}
                      name={`edu-start-${index}`}
                      defaultValue={edu.startDate}
                    />
                    <Input
                      label="Date de fin"
                      id={`edu-end-${index}`}
                      name={`edu-end-${index}`}
                      defaultValue={edu.endDate}
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
                <Button size="sm" variant="secondary">
                  Ajouter
                </Button>
              </div>
              <div className="space-y-3">
                {resume.skills.map((skill, index) => (
                  <div key={index} className="flex items-center">
                    <Input
                      id={`skill-${index}`}
                      name={`skill-${index}`}
                      defaultValue={skill}
                      wrapperClassName="flex-1 mb-0"
                    />
                    <button
                      className="ml-2 text-gray-500 hover:text-error-500"
                      aria-label="Supprimer"
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
                <Button size="sm" variant="secondary">
                  Ajouter
                </Button>
              </div>
              <div className="space-y-3">
                {resume.languages.map((lang, index) => (
                  <div key={index} className="grid grid-cols-2 gap-2 items-center">
                    <Input
                      id={`lang-name-${index}`}
                      name={`lang-name-${index}`}
                      defaultValue={lang.name}
                      wrapperClassName="mb-0"
                    />
                    <div className="flex items-center">
                      <Input
                        id={`lang-level-${index}`}
                        name={`lang-level-${index}`}
                        defaultValue={lang.level}
                        wrapperClassName="flex-1 mb-0"
                      />
                      <button
                        className="ml-2 text-gray-500 hover:text-error-500"
                        aria-label="Supprimer"
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

export default CandidateResume;