// src/app/models/employee.model.ts

export interface Employee {
  id: number;
  name: string;
  designation: string;
  address: string;
  role:string;
  email: string;
  phoneNumber: string;
  registerImage :string;
}

export interface TaskModel {
  id: number;
  project: string;
  taskTitle: string;
  assignedTo: string;
  assignedToName?: string;
  startDate: string;      
  endDate: string;
  priority: string;
  module: string;
  subModule: string;
  screen: string;
  description: string;
  attachmentPath: string;
   DeleteAttchment: string;
}

export interface TimesheetEntry {
  id?: number;
  employeeName: string;
  date: string;
  hours: number;
  project: string;
  status: string;
}

