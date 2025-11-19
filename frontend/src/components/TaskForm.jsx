import { useState } from 'react';
import './TaskForm.css';

const TaskForm = () => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    address: '',
    priority: '',
    duration: ''
  });
  const [successMessage, setSuccessMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSuccessMessage('');

    try {
      const payload = {
        title: formData.title,
        description: formData.description,
        address: formData.address,
        priority: formData.priority,
        duration: formData.duration ? parseInt(formData.duration, 10) : null
      };

      const response = await fetch('http://localhost:8080/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        setSuccessMessage(`Task "${data.title}" created successfully!`);
        // Reset form
        setFormData({
          title: '',
          description: '',
          address: '',
          priority: '',
          duration: ''
        });
      } else {
        setSuccessMessage('Failed to create task. Please try again.');
      }
    } catch (error) {
      setSuccessMessage('Error connecting to server. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="task-form-container">
      <h1>Create New Task</h1>
      <form onSubmit={handleSubmit} className="task-form">
        <div className="form-group">
          <label htmlFor="title">Title *</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
            placeholder="Enter task title"
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Enter task description"
            rows="4"
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">Address *</label>
          <input
            type="text"
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
            placeholder="Enter service address"
          />
        </div>

        <div className="form-group">
          <label htmlFor="priority">Priority *</label>
          <select
            id="priority"
            name="priority"
            value={formData.priority}
            onChange={handleChange}
            required
          >
            <option value="">Select priority</option>
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="duration">Estimated Duration (minutes)</label>
          <input
            type="number"
            id="duration"
            name="duration"
            value={formData.duration}
            onChange={handleChange}
            placeholder="Enter estimated duration"
            min="0"
          />
        </div>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Creating...' : 'Create Task'}
        </button>

        {successMessage && (
          <div className="success-message" role="alert">
            {successMessage}
          </div>
        )}
      </form>
    </div>
  );
};

export default TaskForm;
