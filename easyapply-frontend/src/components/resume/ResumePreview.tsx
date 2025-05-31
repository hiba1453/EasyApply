import React from 'react';
import { Download, Edit } from 'lucide-react';
import { Resume } from '../../types';
import Card from '../ui/Card';
import Button from '../ui/Button';

interface ResumePreviewProps {
  resume: Resume;
  onEdit: () => void;
}

const ResumePreview: React.FC<ResumePreviewProps> = ({ resume, onEdit }) => {
  const { education, experience, skills, languages } = resume;

  return (
    <div className="max-w-3xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold">Mon CV</h2>
        <div className="flex space-x-3">
          <Button variant="secondary" size="sm" onClick={onEdit}>
            <Edit className="w-4 h-4 mr-2" />
            Modifier
          </Button>
          <Button variant="primary" size="sm">
            <Download className="w-4 h-4 mr-2" />
            Télécharger
          </Button>
        </div>
      </div>

      <Card className="bg-white shadow-md border border-gray-200 mb-8">
        <div className="px-8 py-10">
          <header className="border-b border-gray-200 pb-6 mb-6">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Thomas Dubois</h1>
            <p className="text-primary-600 font-medium text-lg mb-3">Développeur Frontend</p>
            <div className="text-gray-600">
              <p>thomas@example.com</p>
              <p>Paris, France</p>
            </div>
          </header>

          <section className="mb-8">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">Expérience professionnelle</h3>
            <div className="space-y-6">
              {experience.map((exp, index) => (
                <div key={index} className="border-l-2 border-primary-200 pl-4">
                  <div className="flex justify-between mb-1">
                    <h4 className="font-semibold text-gray-800">{exp.title}</h4>
                    <span className="text-sm text-gray-500">
                      {exp.startDate} - {exp.endDate === 'Present' ? 'Présent' : exp.endDate}
                    </span>
                  </div>
                  <p className="text-primary-600 mb-1">{exp.company}, {exp.location}</p>
                  <p className="text-gray-600 text-sm">{exp.description}</p>
                </div>
              ))}
            </div>
          </section>

          <section className="mb-8">
            <h3 className="text-xl font-semibold mb-4 text-gray-800">Formation</h3>
            <div className="space-y-6">
              {education.map((edu, index) => (
                <div key={index} className="border-l-2 border-primary-200 pl-4">
                  <div className="flex justify-between mb-1">
                    <h4 className="font-semibold text-gray-800">{edu.degree} en {edu.field}</h4>
                    <span className="text-sm text-gray-500">{edu.startDate} - {edu.endDate}</span>
                  </div>
                  <p className="text-primary-600">{edu.institution}</p>
                </div>
              ))}
            </div>
          </section>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <section>
              <h3 className="text-xl font-semibold mb-4 text-gray-800">Compétences</h3>
              <div className="flex flex-wrap gap-2">
                {skills.map((skill, index) => (
                  <span 
                    key={index} 
                    className="bg-primary-50 text-primary-700 px-3 py-1 rounded-full text-sm font-medium"
                  >
                    {skill}
                  </span>
                ))}
              </div>
            </section>

            <section>
              <h3 className="text-xl font-semibold mb-4 text-gray-800">Langues</h3>
              <ul className="space-y-2">
                {languages.map((lang, index) => (
                  <li key={index} className="flex justify-between">
                    <span className="text-gray-800">{lang.name}</span>
                    <span className="text-gray-600">{lang.level}</span>
                  </li>
                ))}
              </ul>
            </section>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default ResumePreview;