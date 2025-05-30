import React from 'react';
import { Link } from 'react-router-dom';
import { Briefcase, Facebook, Twitter, Instagram, Linkedin } from 'lucide-react';

const Footer: React.FC = () => {
  return (
    <footer className="bg-gray-900 text-white pt-12 pb-8">
      <div className="container mx-auto px-4 md:px-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-8">
          <div>
            <div className="flex items-center space-x-2 mb-4">
              <Briefcase className="w-8 h-8 text-primary-400" />
              <span className="text-xl font-bold text-white">EasyApply</span>
            </div>
            <p className="text-gray-400 mb-4">
              Votre carrière, guidée intelligemment. Trouvez votre prochain emploi avec l'aide de notre plateforme intuitive.
            </p>
            <div className="flex space-x-4">
              <a 
                href="https://facebook.com" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="text-gray-400 hover:text-white transition-colors"
                aria-label="Facebook"
              >
                <Facebook className="w-5 h-5" />
              </a>
              <a 
                href="https://twitter.com" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="text-gray-400 hover:text-white transition-colors"
                aria-label="Twitter"
              >
                <Twitter className="w-5 h-5" />
              </a>
              <a 
                href="https://instagram.com" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="text-gray-400 hover:text-white transition-colors"
                aria-label="Instagram"
              >
                <Instagram className="w-5 h-5" />
              </a>
              <a 
                href="https://linkedin.com" 
                target="_blank" 
                rel="noopener noreferrer" 
                className="text-gray-400 hover:text-white transition-colors"
                aria-label="LinkedIn"
              >
                <Linkedin className="w-5 h-5" />
              </a>
            </div>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Liens rapides</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/" className="text-gray-400 hover:text-white transition-colors">
                  Accueil
                </Link>
              </li>
              <li>
                <Link to="/dashboard/candidate/jobs" className="text-gray-400 hover:text-white transition-colors">
                  Offres d'emploi
                </Link>
              </li>
              <li>
                <Link to="/companies" className="text-gray-400 hover:text-white transition-colors">
                  Entreprises
                </Link>
              </li>
              <li>
                <Link to="/about" className="text-gray-400 hover:text-white transition-colors">
                  À propos
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Pour les candidats</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/login" className="text-gray-400 hover:text-white transition-colors">
                  Se connecter
                </Link>
              </li>
              <li>
                <Link to="/register" className="text-gray-400 hover:text-white transition-colors">
                  Créer un compte
                </Link>
              </li>
              <li>
                <Link to="/dashboard/candidate/jobs" className="text-gray-400 hover:text-white transition-colors">
                  Parcourir les offres
                </Link>
              </li>
              <li>
                <Link to="/resources" className="text-gray-400 hover:text-white transition-colors">
                  Ressources de carrière
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-4">Pour les entreprises</h3>
            <ul className="space-y-2">
              <li>
                <Link to="/business/login" className="text-gray-400 hover:text-white transition-colors">
                  Se connecter
                </Link>
              </li>
              <li>
                <Link to="/business/register" className="text-gray-400 hover:text-white transition-colors">
                  Créer un compte
                </Link>
              </li>
              <li>
                <Link to="/business/post" className="text-gray-400 hover:text-white transition-colors">
                  Publier une offre
                </Link>
              </li>
              <li>
                <Link to="/business/pricing" className="text-gray-400 hover:text-white transition-colors">
                  Tarifs
                </Link>
              </li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-800 pt-6 mt-6">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <p className="text-gray-500 text-sm mb-4 md:mb-0">
              © {new Date().getFullYear()} EasyApply. Tous droits réservés.
            </p>
            <div className="flex space-x-4 text-sm text-gray-500">
              <Link to="/privacy" className="hover:text-white transition-colors">
                Politique de confidentialité
              </Link>
              <Link to="/terms" className="hover:text-white transition-colors">
                Conditions d'utilisation
              </Link>
              <Link to="/cookies" className="hover:text-white transition-colors">
                Cookies
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;