import React from 'react';
import ApplicationList from '../../../components/applications/ApplicationList';
import { mockApplications, mockJobs } from '../../../utils/mockData';

const CandidateApplications: React.FC = () => {
  return (
    <div>
      <ApplicationList applications={mockApplications} jobs={mockJobs} />
    </div>
  );
};

export default CandidateApplications;