import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { Briefcase, FileText, Send, BarChart, Users, Settings, LogOut } from 'lucide-react';

interface SidebarProps {
  userType: 'candidate' | 'company';
}

const Sidebar: React.FC<SidebarProps> = ({ userType }) => {
  const location = useLocation();
  
  return (
    <div className="h-screen flex flex-col bg-white border-r border-gray-200 w-64 fixed left-0 top-0 z-30">
      <div className="flex items-center space-x-2 px-6 py-4 border-b border-gray-200">
        <Briefcase className="w-8 h-8 text-primary-600" />
        <span className="text-xl font-bold text-primary-800">EasyApply</span>
      </div>
      
      <nav className="flex-1 overflow-y-auto py-6 px-4">
        {userType === 'candidate' ? (
          <div className="space-y-2">
            <NavLink
              to="/dashboard/candidate/jobs"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <Briefcase className="w-5 h-5" />
              <span>Offres recommandées</span>
            </NavLink>
            
            <NavLink
              to="/dashboard/candidate/resume"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <FileText className="w-5 h-5" />
              <span>Mon CV</span>
            </NavLink>
            
            <NavLink
              to="/dashboard/candidate/applications"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <Send className="w-5 h-5" />
              <span>Mes candidatures</span>
            </NavLink>
          </div>
        ) : (
          <div className="space-y-2">
            <NavLink
              to="/dashboard/company/jobs"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <Briefcase className="w-5 h-5" />
              <span>Mes offres</span>
            </NavLink>
            
            <NavLink
              to="/dashboard/company/applications"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <Users className="w-5 h-5" />
              <span>Candidatures reçues</span>
            </NavLink>
            
            <NavLink
              to="/dashboard/company/analytics"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <BarChart className="w-5 h-5" />
              <span>Statistiques</span>
            </NavLink>
          </div>
        )}
        
        <div className="mt-8 pt-8 border-t border-gray-200">
          <div className="space-y-2">
            <NavLink
              to="/dashboard/settings"
              className={({ isActive }) => 
                `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                  isActive 
                    ? 'bg-primary-50 text-primary-700' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`
              }
            >
              <Settings className="w-5 h-5" />
              <span>Paramètres</span>
            </NavLink>
            
            <NavLink
              to="/logout"
              className="flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors text-gray-600 hover:bg-gray-100"
            >
              <LogOut className="w-5 h-5" />
              <span>Déconnexion</span>
            </NavLink>
          </div>
        </div>
      </nav>
      
      <div className="p-4 border-t border-gray-200">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-full bg-primary-100 flex items-center justify-center text-primary-700 font-medium">
            {userType === 'candidate' ? 'TD' : 'CM'}
          </div>
          <div>
            <p className="font-medium text-gray-800">
              {userType === 'candidate' ? 'Thomas Dubois' : 'Claire Martin'}
            </p>
            <p className="text-xs text-gray-500">
              {userType === 'candidate' ? 'Candidat' : 'Recruteur'}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;