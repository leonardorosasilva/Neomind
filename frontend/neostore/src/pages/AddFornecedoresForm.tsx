import React, { useState } from 'react';

interface AddFornecedoresFormProps {
  onSave?: (data: {
    name: string;
    email: string;
    description: string;
    cnpj: string;
  }) => void;
}

const AddFornecedoresForm: React.FC<AddFornecedoresFormProps> = ({ onSave }) => {
  const [name, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [description, setDescription] = useState('');
  const [cnpj, setCnpj] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSave) {
      onSave({ name, email, description, cnpj });
    }
  };

  return (
    <section className="max-w-4xl p-6 mx-auto bg-white rounded-md shadow-md ">
      <h2 className="text-lg font-semibold text-gray-700 capitalize ">Adicionando Fornecedor</h2>

      <form onSubmit={handleSubmit}>
        <div className="grid grid-cols-1 gap-6 mt-4 sm:grid-cols-2">
          <div>
            <label htmlFor="name" className="text-gray-700 ">Nome</label>
            <input
              id="name"
              type="text"
              value={name}
              onChange={(e) => setUsername(e.target.value)}
              className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-200 rounded-md 
                            
                         focus:border-blue-400 focus:ring-blue-300 focus:ring-opacity-40 
                          focus:outline-none focus:ring"
            />
          </div>

          <div>
            <label htmlFor="email" className="text-gray-700 ">Email</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-200 rounded-md 
                            
                         focus:border-blue-400 focus:ring-blue-300 focus:ring-opacity-40 
                          focus:outline-none focus:ring"
            />
          </div>

          <div>
            <label htmlFor="description" className="text-gray-700 ">Descrição</label>
            <input
              id="description"
              type="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-200 rounded-md 
                            
                         focus:border-blue-400 focus:ring-blue-300 focus:ring-opacity-40 
                          focus:outline-none focus:ring"
            />
          </div>

          <div>
            <label htmlFor="cnpj" className="text-gray-700 ">CNPJ</label>
            <input
              id="cnpj"
              type="text"
              value={cnpj}
              onChange={(e) => setCnpj(e.target.value)}
              className="block w-full px-4 py-2 mt-2 text-gray-700 bg-white border border-gray-200 rounded-md 
                            
                         focus:border-blue-400 focus:ring-blue-300 focus:ring-opacity-40 
                          focus:outline-none focus:ring"
            />
          </div>
        </div>

        <div className="flex justify-end mt-6">
          <button
            type="submit"
            className="px-8 py-2.5 leading-5 text-white transition-colors duration-300 transform 
                       bg-gray-700 rounded-md hover:bg-gray-600 focus:outline-none focus:bg-gray-600"
          >
            Save
          </button>
        </div>
      </form>
    </section>
  );
};

export default AddFornecedoresForm;
