import React from 'react';
import { Link } from 'react-router-dom';
import { Briefcase as BriefcaseBusiness, BrainCircuit, FileText, Users, ChevronRight, Building2, Search, Briefcase } from 'lucide-react';
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';
import Button from '../components/ui/Button';

const Landing: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <Header />
      
      {/* Hero Section */}
      <section className="pt-32 pb-20 md:pt-40 md:pb-24 bg-gradient-to-b from-gray-50 to-white">
        <div className="container mx-auto px-4 md:px-6">
          <div className="flex flex-col lg:flex-row items-center">
            <div className="w-full lg:w-1/2 mb-10 lg:mb-0">
              <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4 leading-tight">
                Votre carrière,{' '}
                <span className="text-primary-600">guidée intelligemment</span>
              </h1>
              <p className="text-xl text-gray-600 mb-8 max-w-lg">
                Trouvez votre emploi idéal grâce à des recommandations personnalisées, un CV optimisé et un réseau professionnel enrichissant.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <Link to="/register">
                  <Button size="lg">S'inscrire</Button>
                </Link>
                <Link to="/login">
                  <Button variant="secondary" size="lg">Se connecter</Button>
                </Link>
              </div>
            </div>
            <div className="w-full lg:w-1/2 flex justify-center lg:justify-end">
              <img
                src="https://images.pexels.com/photos/3184291/pexels-photo-3184291.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
                alt="Professionnels collaborant"
                className="rounded-lg shadow-xl w-full max-w-lg object-cover h-96"
              />
            </div>
          </div>
        </div>
      </section>
      
      {/* Features Section */}
      <section className="py-16 md:py-24 bg-white">
        <div className="container mx-auto px-4 md:px-6">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Fonctionnalités adaptées à vos besoins
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              EasyApply vous accompagne à chaque étape de votre parcours professionnel
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Feature 1 */}
            <div className="bg-white rounded-lg p-8 shadow-sm border border-gray-100 transition-all duration-300 hover:shadow-md">
              <div className="w-14 h-14 bg-primary-50 rounded-lg flex items-center justify-center mb-6">
                <BrainCircuit className="w-7 h-7 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-3">Recommandations IA</h3>
              <p className="text-gray-600 mb-4">
                Recevez des offres d'emploi adaptées à votre profil grâce à notre algorithme intelligent d'analyse de compétences.
              </p>
              <Link to="/features/ai" className="inline-flex items-center text-primary-600 font-medium">
                En savoir plus <ChevronRight className="w-4 h-4 ml-1" />
              </Link>
            </div>
            
            {/* Feature 2 */}
            <div className="bg-white rounded-lg p-8 shadow-sm border border-gray-100 transition-all duration-300 hover:shadow-md">
              <div className="w-14 h-14 bg-primary-50 rounded-lg flex items-center justify-center mb-6">
                <FileText className="w-7 h-7 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-3">Création de CV</h3>
              <p className="text-gray-600 mb-4">
                Créez et optimisez votre CV pour maximiser vos chances d'être remarqué par les recruteurs.
              </p>
              <Link to="/features/resume" className="inline-flex items-center text-primary-600 font-medium">
                En savoir plus <ChevronRight className="w-4 h-4 ml-1" />
              </Link>
            </div>
            
            {/* Feature 3 */}
            <div className="bg-white rounded-lg p-8 shadow-sm border border-gray-100 transition-all duration-300 hover:shadow-md">
              <div className="w-14 h-14 bg-primary-50 rounded-lg flex items-center justify-center mb-6">
                <Users className="w-7 h-7 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-3">Réseau professionnel</h3>
              <p className="text-gray-600 mb-4">
                Connectez-vous avec des professionnels de votre secteur et développez votre réseau pour saisir de nouvelles opportunités.
              </p>
              <Link to="/features/network" className="inline-flex items-center text-primary-600 font-medium">
                En savoir plus <ChevronRight className="w-4 h-4 ml-1" />
              </Link>
            </div>
          </div>
        </div>
      </section>
      
      {/* How It Works Section */}
      <section className="py-16 md:py-24 bg-gray-50">
        <div className="container mx-auto px-4 md:px-6">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Comment ça fonctionne
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              Un processus simple pour trouver votre emploi idéal
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-5xl mx-auto">
            {/* Step 1 */}
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-primary-700 text-xl font-bold">1</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Créez votre profil</h3>
              <p className="text-gray-600">
                Inscrivez-vous et importez votre CV ou créez-en un nouveau avec notre outil intuitif.
              </p>
            </div>
            
            {/* Step 2 */}
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-primary-700 text-xl font-bold">2</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Explorez les offres</h3>
              <p className="text-gray-600">
                Parcourez les offres recommandées et filtrez selon vos préférences.
              </p>
            </div>
            
            {/* Step 3 */}
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-primary-700 text-xl font-bold">3</span>
              </div>
              <h3 className="text-xl font-semibold mb-2">Postulez facilement</h3>
              <p className="text-gray-600">
                Envoyez votre candidature en quelques clics et suivez son statut.
              </p>
            </div>
          </div>
        </div>
      </section>
      
      {/* Companies Section */}
      <section className="py-16 md:py-24 bg-white">
        <div className="container mx-auto px-4 md:px-6">
          <div className="text-center mb-16">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
              Entreprises partenaires
            </h2>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              Rejoignez des milliers de candidats qui ont trouvé leur emploi idéal
            </p>
          </div>
          
          <div className="grid grid-cols-2 md:grid-cols-5 gap-8 items-center justify-items-center">
            <div className="grayscale hover:grayscale-0 transition-all duration-300">
              <img src="https://images.pexels.com/photos/15031232/pexels-photo-15031232.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="TechStart" className="h-12 object-contain" />
            </div>
            <div className="grayscale hover:grayscale-0 transition-all duration-300">
              <img src="https://images.pexels.com/photos/6224/hands-people-woman-working.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="DesignLab" className="h-12 object-contain" />
            </div>
            <div className="grayscale hover:grayscale-0 transition-all duration-300">
              <img src="https://images.pexels.com/photos/1181467/pexels-photo-1181467.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="DataInsight" className="h-12 object-contain" />
            </div>
            <div className="grayscale hover:grayscale-0 transition-all duration-300">
              <img src="https://images.pexels.com/photos/1148820/pexels-photo-1148820.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="CloudNative" className="h-12 object-contain" />
            </div>
            <div className="grayscale hover:grayscale-0 transition-all duration-300">
              <img src="https://images.pexels.com/photos/3183186/pexels-photo-3183186.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2" alt="ProductHive" className="h-12 object-contain" />
            </div>
          </div>
        </div>
      </section>
      
      {/* CTA Section */}
      <section className="py-16 md:py-20 bg-primary-800">
        <div className="container mx-auto px-4 md:px-6">
          <div className="flex flex-col lg:flex-row items-center justify-between">
            <div className="mb-8 lg:mb-0 lg:max-w-xl">
              <h2 className="text-3xl md:text-4xl font-bold text-white mb-4">
                Prêt à donner un nouvel élan à votre carrière ?
              </h2>
              <p className="text-lg text-primary-100 mb-6">
                Rejoignez EasyApply aujourd'hui et découvrez des opportunités adaptées à votre profil et à vos ambitions.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <Link to="/register">
                  <Button size="lg" className="bg-white hover:bg-gray-100 text-primary-800">
                    Créer un compte
                  </Button>
                </Link>
                <Link to="/jobs">
                  <Button 
                    variant="outline" 
                    size="lg" 
                    className="border-white text-white hover:bg-primary-700"
                  >
                    Explorer les offres
                  </Button>
                </Link>
              </div>
            </div>
            <div className="lg:w-96 flex justify-center">
              <img
                src="https://images.pexels.com/photos/3194518/pexels-photo-3194518.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
                alt="Professionnels heureux"
                className="rounded-lg shadow-xl object-cover h-64 w-full"
              />
            </div>
          </div>
        </div>
      </section>
      
      <Footer />
    </div>
  );
};

export default Landing;