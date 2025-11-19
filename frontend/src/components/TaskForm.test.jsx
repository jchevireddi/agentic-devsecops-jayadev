import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TaskForm from './TaskForm';

// Mock fetch globally
global.fetch = vi.fn();

describe('TaskForm', () => {
  beforeEach(() => {
    // Reset mocks before each test
    vi.clearAllMocks();
  });

  it('renders the form with all required fields', () => {
    render(<TaskForm />);
    
    expect(screen.getByText('Create New Task')).toBeInTheDocument();
    expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/description/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/address/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/priority/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/estimated duration/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /create task/i })).toBeInTheDocument();
  });

  it('updates form fields when user types', async () => {
    const user = userEvent.setup();
    render(<TaskForm />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const descriptionInput = screen.getByLabelText(/description/i);
    const addressInput = screen.getByLabelText(/address/i);
    const durationInput = screen.getByLabelText(/estimated duration/i);
    
    await user.type(titleInput, 'Fix HVAC System');
    await user.type(descriptionInput, 'AC not cooling');
    await user.type(addressInput, '123 Main St');
    await user.type(durationInput, '120');
    
    expect(titleInput).toHaveValue('Fix HVAC System');
    expect(descriptionInput).toHaveValue('AC not cooling');
    expect(addressInput).toHaveValue('123 Main St');
    expect(durationInput).toHaveValue(120);
  });

  it('updates priority when user selects an option', async () => {
    const user = userEvent.setup();
    render(<TaskForm />);
    
    const prioritySelect = screen.getByLabelText(/priority/i);
    await user.selectOptions(prioritySelect, 'HIGH');
    
    expect(prioritySelect).toHaveValue('HIGH');
  });

  it('displays all priority options', () => {
    render(<TaskForm />);
    
    const prioritySelect = screen.getByLabelText(/priority/i);
    const options = Array.from(prioritySelect.options).map(option => option.value);
    
    expect(options).toContain('');
    expect(options).toContain('LOW');
    expect(options).toContain('MEDIUM');
    expect(options).toContain('HIGH');
  });

  it('submits form with valid data and displays success message', async () => {
    const user = userEvent.setup();
    const mockResponse = {
      id: '123',
      title: 'Fix HVAC System',
      description: 'AC not cooling',
      address: '123 Main St',
      priority: 'HIGH',
      duration: 120
    };
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Fix HVAC System');
    await user.type(screen.getByLabelText(/description/i), 'AC not cooling');
    await user.type(screen.getByLabelText(/address/i), '123 Main St');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    await user.type(screen.getByLabelText(/estimated duration/i), '120');
    
    const submitButton = screen.getByRole('button', { name: /create task/i });
    await user.click(submitButton);
    
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: 'Fix HVAC System',
          description: 'AC not cooling',
          address: '123 Main St',
          priority: 'HIGH',
          duration: 120
        }),
      });
    });
    
    await waitFor(() => {
      expect(screen.getByText(/Task "Fix HVAC System" created successfully!/i)).toBeInTheDocument();
    });
  });

  it('resets form after successful submission', async () => {
    const user = userEvent.setup();
    const mockResponse = {
      id: '123',
      title: 'Test Task',
      description: 'Test Description',
      address: 'Test Address',
      priority: 'HIGH',
      duration: 60
    };
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Task');
    await user.type(screen.getByLabelText(/description/i), 'Test Description');
    await user.type(screen.getByLabelText(/address/i), 'Test Address');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    await user.type(screen.getByLabelText(/estimated duration/i), '60');
    
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(screen.getByLabelText(/title/i)).toHaveValue('');
      expect(screen.getByLabelText(/description/i)).toHaveValue('');
      expect(screen.getByLabelText(/address/i)).toHaveValue('');
      expect(screen.getByLabelText(/priority/i)).toHaveValue('');
      expect(screen.getByLabelText(/estimated duration/i)).toHaveValue(null);
    });
  });

  it('disables submit button while submitting', async () => {
    const user = userEvent.setup();
    
    global.fetch.mockImplementationOnce(() => 
      new Promise(resolve => setTimeout(() => resolve({
        ok: true,
        json: async () => ({ id: '123', title: 'Test' })
      }), 100))
    );
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Task');
    await user.type(screen.getByLabelText(/address/i), 'Test Address');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    
    const submitButton = screen.getByRole('button', { name: /create task/i });
    await user.click(submitButton);
    
    expect(submitButton).toBeDisabled();
    expect(submitButton).toHaveTextContent('Creating...');
    
    await waitFor(() => {
      expect(submitButton).not.toBeDisabled();
      expect(submitButton).toHaveTextContent('Create Task');
    });
  });

  it('displays error message when submission fails', async () => {
    const user = userEvent.setup();
    
    global.fetch.mockResolvedValueOnce({
      ok: false,
    });
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Task');
    await user.type(screen.getByLabelText(/address/i), 'Test Address');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/Failed to create task. Please try again./i)).toBeInTheDocument();
    });
  });

  it('displays error message when fetch throws an error', async () => {
    const user = userEvent.setup();
    
    global.fetch.mockRejectedValueOnce(new Error('Network error'));
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Test Task');
    await user.type(screen.getByLabelText(/address/i), 'Test Address');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/Error connecting to server. Please try again./i)).toBeInTheDocument();
    });
  });

  it('handles form submission without optional fields', async () => {
    const user = userEvent.setup();
    
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ 
        id: '123', 
        title: 'Minimal Task',
        address: '456 Oak St',
        priority: 'LOW'
      }),
    });
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Minimal Task');
    await user.type(screen.getByLabelText(/address/i), '456 Oak St');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'LOW');
    
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: 'Minimal Task',
          description: '',
          address: '456 Oak St',
          priority: 'LOW',
          duration: null
        }),
      });
    });
  });

  it('prevents form submission when required fields are empty', async () => {
    render(<TaskForm />);
    
    const titleInput = screen.getByLabelText(/title/i);
    const addressInput = screen.getByLabelText(/address/i);
    const prioritySelect = screen.getByLabelText(/priority/i);
    
    expect(titleInput).toBeRequired();
    expect(addressInput).toBeRequired();
    expect(prioritySelect).toBeRequired();
  });

  it('clears success message on new form submission', async () => {
    const user = userEvent.setup();
    
    // First submission - success
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ id: '123', title: 'Task 1' }),
    });
    
    render(<TaskForm />);
    
    await user.type(screen.getByLabelText(/title/i), 'Task 1');
    await user.type(screen.getByLabelText(/address/i), 'Address 1');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'HIGH');
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/Task "Task 1" created successfully!/i)).toBeInTheDocument();
    });
    
    // Second submission - should clear first message
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ id: '456', title: 'Task 2' }),
    });
    
    await user.type(screen.getByLabelText(/title/i), 'Task 2');
    await user.type(screen.getByLabelText(/address/i), 'Address 2');
    await user.selectOptions(screen.getByLabelText(/priority/i), 'MEDIUM');
    
    // Message should be cleared immediately on submit, before showing new message
    await user.click(screen.getByRole('button', { name: /create task/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/Task "Task 2" created successfully!/i)).toBeInTheDocument();
    });
  });
});
