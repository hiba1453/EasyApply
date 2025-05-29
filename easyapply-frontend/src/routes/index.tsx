import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Landing from '../pages/Landing';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';

import DashboardLayout from '../components/layout/DashboardLayout';
import CandidateJobs from '../pages/dashboard/candidate/CandidateJobs';
import CandidateResume from '../pages/dashboard/candidate/CandidateResume';
import CandidateApplications from '../pages/dashboard/candidate/CandidateApplications';
import CompanyJobs from '../pages/dashboard/company/CompanyJobs';

const Router: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Candidate dashboard routes */}
        <Route 
          path="/dashboard/candidate/jobs" 
          element={
            <DashboardLayout>
              <CandidateJobs />
            </DashboardLayout>
          } 
        />
        <Route 
          path="/dashboard/candidate/resume" 
          element={
            <DashboardLayout>
              <CandidateResume />
            </DashboardLayout>
          } 
        />
        <Route 
          path="/dashboard/candidate/applications" 
          element={
            <DashboardLayout>
              <CandidateApplications />
            </DashboardLayout>
          } 
        />
        
        {/* Company dashboard routes */}
        <Route 
          path="/dashboard/company/jobs" 
          element={
            <DashboardLayout>
              <CompanyJobs />
            </DashboardLayout>
          } 
        />
        
        {/* Redirects */}
        <Route path="/dashboard" element={<Navigate to="/dashboard/candidate/jobs\" replace />} />
        <Route path="*" element={<Navigate to="/\" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;