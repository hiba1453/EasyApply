import React from 'react';
import Sidebar from './Sidebar';
import { useLocation } from 'react-router-dom';

interface DashboardLayoutProps {
  children: React.ReactNode;
}

const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const location = useLocation();
  const userType = location.pathname.includes('/candidate') ? 'candidate' : 'company';
  let companyName = undefined;
  if (userType === 'company') {
    companyName = localStorage.getItem('companyName') || undefined;
  }
  
  return (
    <div className="min-h-screen bg-gray-50 flex">
      <Sidebar userType={userType} companyName={companyName} />
      <div className="flex-1 ml-64">
        <main className="p-6 md:p-10">
          {children}
        </main>
      </div>
    </div>
  );
};

export default DashboardLayout;