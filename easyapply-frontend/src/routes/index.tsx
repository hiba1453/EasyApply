import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Landing from '../pages/Landing';
import Login from '../pages/auth/Login';
import Register from '../pages/auth/Register';
import CompanyDashboard from '../pages/dashboard/company/CompanyDashboard';
import JobForm from '../pages/dashboard/company/JobForm';
import DashboardLayout from '../components/layout/DashboardLayout';
import CandidateJobs from '../pages/dashboard/candidate/CandidateJobs';
import CandidateResume from '../pages/dashboard/candidate/CandidateResume';
import CandidateApplications from '../pages/dashboard/candidate/CandidateApplications';
import CompanyJobs from '../pages/dashboard/company/CompanyJobs';
import RegisterCompany from '../pages/auth/RegisterCompany';

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
          path="/dashboard/company" 
          element={
            <DashboardLayout>
              <CompanyDashboard />
            </DashboardLayout>
          } 
        />
        <Route 
          path="/dashboard/company/jobs" 
          element={
            <DashboardLayout>
              <CompanyJobs />
            </DashboardLayout>
          } 
        />
        <Route 
          path="/dashboard/company/jobs/create" 
          element={
            <DashboardLayout>
              <JobForm />
            </DashboardLayout>
          } 
        />
        <Route 
          path="/dashboard/company/jobs/edit/:id" 
          element={
            <DashboardLayout>
              <JobForm />
            </DashboardLayout>
          } 
        />

        <Route
         path="/register/company"
         element={<RegisterCompany />} 
        />
        
        {/* Redirects */}
        <Route path="/dashboard" element={<Navigate to="/dashboard/candidate/jobs" replace />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;