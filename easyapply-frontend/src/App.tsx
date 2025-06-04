import React from 'react';
import Router from './routes';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginCallback from './components/auth/LoginCallback';
import CandidateDashboard from './pages/dashboard/candidate/CandidateJobs'; // Your dashboard component
import Login from './pages/auth/Login';
import Logout from './pages/auth/logout';
import Register from './pages/auth/Register';
import RegisterCompany from './pages/auth/RegisterCompany';

import Landing from './pages/Landing'; 
function App() {
  return (
    <Router/>
  );
}

export default App;