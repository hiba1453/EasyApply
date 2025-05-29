import { JobPosting, Application, Resume, Company, User } from '../types';

export const mockJobs: JobPosting[] = [
  {
    id: '1',
    title: 'Développeur Frontend React',
    company: 'TechStart',
    location: 'Paris, France',
    salary: '45 000 € - 60 000 €',
    description: 'Nous recherchons un développeur Frontend React passionné pour rejoindre notre équipe dynamique. Vous serez responsable de la création d\'interfaces utilisateur réactives et intuitives pour nos produits. Vous travaillerez en étroite collaboration avec notre équipe de designers et de développeurs backend pour créer des expériences utilisateur exceptionnelles.',
    tags: ['React', 'JavaScript', 'TypeScript', 'CDI'],
    postedDate: '2023-04-15',
  },
  {
    id: '2',
    title: 'UX/UI Designer',
    company: 'DesignLab',
    location: 'Lyon, France',
    salary: '40 000 € - 55 000 €',
    description: 'Rejoignez notre studio de design pour créer des expériences utilisateurs exceptionnelles. Vous serez responsable de la conception d\'interfaces utilisateur pour des applications web et mobiles. Vous travaillerez en étroite collaboration avec notre équipe de développeurs pour transformer vos designs en produits fonctionnels.',
    tags: ['Figma', 'Adobe XD', 'Sketch', 'CDI'],
    postedDate: '2023-04-12',
  },
  {
    id: '3',
    title: 'Data Scientist',
    company: 'DataInsight',
    location: 'Remote',
    salary: '50 000 € - 70 000 €',
    description: 'Nous cherchons un Data Scientist pour analyser et interpréter des données complexes. Vous serez responsable de la création de modèles prédictifs et de l\'analyse de grandes quantités de données. Vous travaillerez en étroite collaboration avec notre équipe de développeurs et de business analysts pour transformer vos insights en fonctionnalités produit.',
    tags: ['Python', 'Machine Learning', 'SQL', 'CDI'],
    postedDate: '2023-04-10',
  },
  {
    id: '4',
    title: 'DevOps Engineer',
    company: 'CloudNative',
    location: 'Nantes, France',
    salary: '45 000 € - 65 000 €',
    description: 'Rejoignez notre équipe pour aider à construire et maintenir notre infrastructure cloud. Vous serez responsable de la mise en place et de la maintenance de nos pipelines CI/CD et de nos environnements de déploiement. Vous travaillerez en étroite collaboration avec nos équipes de développement pour assurer des déploiements rapides et fiables.',
    tags: ['Docker', 'Kubernetes', 'AWS', 'CDI'],
    postedDate: '2023-04-08',
  },
  {
    id: '5',
    title: 'Product Manager',
    company: 'ProductHive',
    location: 'Bordeaux, France',
    salary: '55 000 € - 75 000 €',
    description: 'Nous recherchons un Product Manager expérimenté pour diriger le développement de nos produits. Vous serez responsable de la définition de la roadmap produit et de la priorisation des fonctionnalités. Vous travaillerez en étroite collaboration avec nos équipes de développement, de design et de marketing pour assurer le succès de nos produits.',
    tags: ['Product Management', 'Agile', 'Scrum', 'CDI'],
    postedDate: '2023-04-05',
  },
];

export const mockCompanies: Company[] = [
  {
    id: '1',
    name: 'TechStart',
    logo: 'https://images.pexels.com/photos/15031232/pexels-photo-15031232.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
    industry: 'Tech',
    size: '10-50',
    description: 'TechStart est une startup innovante spécialisée dans le développement de solutions SaaS pour les entreprises.'
  },
  {
    id: '2',
    name: 'DesignLab',
    logo: 'https://images.pexels.com/photos/6224/hands-people-woman-working.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
    industry: 'Design',
    size: '10-50',
    description: 'DesignLab est un studio de design spécialisé dans la création d\'expériences utilisateur exceptionnelles.'
  },
  {
    id: '3',
    name: 'DataInsight',
    logo: 'https://images.pexels.com/photos/1181467/pexels-photo-1181467.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
    industry: 'Data',
    size: '50-200',
    description: 'DataInsight est une entreprise spécialisée dans l\'analyse de données et le machine learning.'
  },
  {
    id: '4',
    name: 'CloudNative',
    logo: 'https://images.pexels.com/photos/1148820/pexels-photo-1148820.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
    industry: 'Cloud',
    size: '50-200',
    description: 'CloudNative est une entreprise spécialisée dans les solutions d\'infrastructure cloud pour les entreprises.'
  },
  {
    id: '5',
    name: 'ProductHive',
    logo: 'https://images.pexels.com/photos/3183186/pexels-photo-3183186.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2',
    industry: 'Product',
    size: '10-50',
    description: 'ProductHive est une entreprise spécialisée dans le développement de produits innovants.'
  },
];

export const mockApplications: Application[] = [
  {
    id: '1',
    jobId: '1',
    status: 'pending',
    appliedDate: '2023-04-16',
  },
  {
    id: '2',
    jobId: '3',
    status: 'reviewed',
    appliedDate: '2023-04-11',
  },
];

export const mockResume: Resume = {
  id: '1',
  userId: '1',
  education: [
    {
      institution: 'Université Paris-Saclay',
      degree: 'Master',
      field: 'Informatique',
      startDate: '2018-09',
      endDate: '2020-06',
    },
    {
      institution: 'IUT de Paris',
      degree: 'License',
      field: 'Informatique',
      startDate: '2015-09',
      endDate: '2018-06',
    },
  ],
  experience: [
    {
      company: 'TechCorp',
      title: 'Développeur Frontend',
      location: 'Paris, France',
      startDate: '2020-09',
      endDate: 'Present',
      description: 'Développement d\'applications web avec React, TypeScript et GraphQL.',
    },
    {
      company: 'StartupXYZ',
      title: 'Stage Développeur Web',
      location: 'Paris, France',
      startDate: '2019-03',
      endDate: '2019-08',
      description: 'Développement de fonctionnalités frontend et backend pour une application SaaS.',
    },
  ],
  skills: ['JavaScript', 'TypeScript', 'React', 'Node.js', 'GraphQL', 'HTML/CSS', 'Git'],
  languages: [
    {
      name: 'Français',
      level: 'Natif',
    },
    {
      name: 'Anglais',
      level: 'Courant',
    },
  ],
};

export const mockUsers: User[] = [
  {
    id: '1',
    name: 'Thomas Dubois',
    email: 'thomas@example.com',
    type: 'candidate',
  },
  {
    id: '2',
    name: 'Claire Martin',
    email: 'claire@designlab.com',
    type: 'company',
  },
];