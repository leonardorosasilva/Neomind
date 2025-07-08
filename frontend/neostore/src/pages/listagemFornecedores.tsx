import { useEffect, useState, useRef } from "react"
import AddFornecedoresForm from "./AddFornecedoresForm";

type Fornecedor = {
    id?: string;
    name: string;
    cnpj: string;
    email: string;
    description: string;
    [key: string]: any;
};

export default function ListagemFornecedores(){
    const [fornecedores, setFornecedores] = useState<Fornecedor[]>([])
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [openDropdownIndex, setOpenDropdownIndex] = useState<number | null>(null);
    const [showForm, setShowForm] = useState(false);
    const [showImportModal, setShowImportModal] = useState(false);
    const [fornecedorEditando, setFornecedorEditando] = useState<Fornecedor | undefined>(undefined);
    const [jsonText, setJsonText] = useState('');
    const [importError, setImportError] = useState<string | null>(null);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const exampleJson = `[
        {
            "name": "Empresa Exemplo LTDA",
            "cnpj": "12.345.678/0001-90",
            "email": "contato@exemplo.com",
            "description": "Empresa especializada em tecnologia"
        },
        {
            "name": "Fornecedor ABC",
            "cnpj": "98.765.432/0001-10",
            "email": "abc@fornecedor.com",
            "description": "Fornecedor de materiais"
        }
    ]`;

    const validateFornecedorStructure = (obj: any): obj is Fornecedor => {
        return (
            typeof obj === 'object' &&
            obj !== null &&
            typeof obj.name === 'string' &&
            typeof obj.cnpj === 'string' &&
            typeof obj.email === 'string' &&
            (obj.description === undefined || typeof obj.description === 'string')
        );
    };

    // Função para importar JSON
    const handleImportJson = () => {
        try {
            setImportError(null);
            
            if (!jsonText.trim()) {
                setImportError('Por favor, insira um JSON válido');
                return;
            }

            const data = JSON.parse(jsonText);
            
            // Verifica se é um array
            if (!Array.isArray(data)) {
                setImportError('O JSON deve ser um array de fornecedores');
                return;
            }

            // Valida estrutura de cada fornecedor
            const invalidItems = data.filter((item, index) => !validateFornecedorStructure(item));
            
            if (invalidItems.length > 0) {
                setImportError(`Estrutura inválida encontrada. Cada fornecedor deve ter: name, cnpj, email e opcionalmente description`);
                return;
            }

            // Adiciona os fornecedores à lista
            const newFornecedores = data.map((item: Fornecedor) => ({
                ...item,
                id: item.id || `temp-${Date.now()}-${Math.random()}`, // Gera ID temporário se não existir
                description: item.description && item.description.length > 50 
                    ? item.description.slice(0, 20) + '...' 
                    : item.description || ''
            }));

            setFornecedores(prev => [...prev, ...newFornecedores]);
            setShowImportModal(false);
            setJsonText('');
            alert(`${newFornecedores.length} fornecedores importados com sucesso!`);

        } catch (e) {
            setImportError('JSON inválido. Verifique a sintaxe.');
        }
    };

    // Função para importar arquivo JSON
    const handleFileImport = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (!file) return;

        if (file.type !== 'application/json') {
            setImportError('Por favor, selecione um arquivo JSON válido');
            return;
        }

        const reader = new FileReader();
        reader.onload = (e) => {
            const content = e.target?.result as string;
            setJsonText(content);
        };
        reader.readAsText(file);
    };

    // Função para fechar modal de importação
    const closeImportModal = () => {
        setShowImportModal(false);
        setJsonText('');
        setImportError(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
    };

    // Fetch fornecedores - only runs once on mount
    useEffect(() => {
        const fetchFornecedores = async () => {
            setIsLoading(true);
            try {
                const response = await fetch('http://localhost:8080/Neomind-1.0-SNAPSHOT/api/fornecedores/');
                
                if (!response.ok) {
                    throw new Error(`Erro HTTP! Status ${response.status}`);
                }
                
                const data = await response.json();
                console.log('data', data);
                
                // Process descriptions here
                const processedData = data.map((fornecedor: Fornecedor) => ({
                    ...fornecedor,
                    description: fornecedor.description && fornecedor.description.length > 50 
                        ? fornecedor.description.slice(0, 20) + '...'  
                        :  fornecedor.description
                }));


                
                setFornecedores(processedData);
            } catch (e) {
                console.error(e);
                setError(e instanceof Error ? e.message : 'Erro desconhecido');
            } finally {
                setIsLoading(false);
            }
        };

        fetchFornecedores();
    }, []);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setOpenDropdownIndex(null);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleEdit = (fornecedor: Fornecedor) => {
        setFornecedorEditando(fornecedor);
        setShowForm(true);
        setOpenDropdownIndex(null);
    };

    const handleDelete = async (id: string) => {
        if (window.confirm('Tem certeza que deseja deletar este fornecedor?')) {
            try {
                const response = await fetch(`http://localhost:8080/Neomind-1.0-SNAPSHOT/api/fornecedores/${id}`, {
                    method: 'DELETE'
                });

                if (!response.ok) {
                    throw new Error('Erro ao deletar fornecedor');
                }

                setFornecedores(prev => prev.filter(f => f.id !== id));
                setOpenDropdownIndex(null);
            } catch (error) {
                console.error(error);
                alert('Erro ao deletar fornecedor');
            }
        }
    };

    const closeForm = () => {
        setShowForm(false);
        setFornecedorEditando(undefined);
    };

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="text-lg">Carregando...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="text-lg text-red-600">Erro: {error}</div>
            </div>
        );
    }

    return(
        <section className="container px-4 mx-auto">
            <div className="sm:flex sm:items-center sm:justify-between">
                <div>
                    <div className="flex items-center gap-x-3">
                        <h2 className="text-lg font-medium text-gray-800">Fornecedores</h2>
                        <span className="px-3 py-1 text-xs text-blue-600 bg-blue-100 rounded-full">
                            {fornecedores.length} fornecedores
                        </span>
                    </div>
                </div>

                <div className="flex items-center mt-4 gap-x-3">
                    <button onClick={() => setShowImportModal(true)}
                     className="flex items-center justify-center w-1/2 px-5 py-2 text-sm text-gray-700 transition-colors duration-200 bg-white border rounded-lg gap-x-2 sm:w-auto hover:bg-gray-100">
                        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <g clipPath="url(#clip0_3098_154395)">
                            <path d="M13.3333 13.3332L9.99997 9.9999M9.99997 9.9999L6.66663 13.3332M9.99997 9.9999V17.4999M16.9916 15.3249C17.8044 14.8818 18.4465 14.1806 18.8165 13.3321C19.1866 12.4835 19.2635 11.5359 19.0351 10.6388C18.8068 9.7417 18.2862 8.94616 17.5555 8.37778C16.8248 7.80939 15.9257 7.50052 15 7.4999H13.95C13.6977 6.52427 13.2276 5.61852 12.5749 4.85073C11.9222 4.08295 11.104 3.47311 10.1817 3.06708C9.25943 2.66104 8.25709 2.46937 7.25006 2.50647C6.24304 2.54358 5.25752 2.80849 4.36761 3.28129C3.47771 3.7541 2.70656 4.42249 2.11215 5.23622C1.51774 6.04996 1.11554 6.98785 0.935783 7.9794C0.756025 8.97095 0.803388 9.99035 1.07431 10.961C1.34523 11.9316 1.83267 12.8281 2.49997 13.5832" stroke="currentColor" strokeWidth="1.67" strokeLinecap="round" strokeLinejoin="round"/>
                            </g>
                            <defs>
                            <clipPath id="clip0_3098_154395">
                            <rect width="20" height="20" fill="white"/>
                            </clipPath>
                            </defs>
                        </svg>
                        <span>Importar JSON</span>
                    </button>

                    <button
                        onClick={() => setShowForm(true)}
                        className="flex cursor-pointer items-center justify-center w-1/2 px-5 py-2 text-sm tracking-wide text-white transition-colors duration-200 bg-blue-500 rounded-lg shrink-0 sm:w-auto gap-x-2 hover:bg-blue-600"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-5 h-5">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <span>Adicionar Fornecedor</span>
                    </button>
                </div>
            </div>

            <div className="mt-6 md:flex md:items-center md:justify-between">
                <div className="relative flex items-center mt-4 md:mt-0">
                    <span className="absolute">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-5 h-5 mx-3 text-gray-400">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
                        </svg>
                    </span>
                    <input 
                        type="text" 
                        placeholder="Search" 
                        className="block w-full py-1.5 pr-5 text-gray-700 bg-white border border-gray-200 rounded-lg md:w-80 placeholder-gray-400/70 pl-11 rtl:pr-11 rtl:pl-5 focus:border-blue-400 focus:ring-blue-300 focus:outline-none focus:ring focus:ring-opacity-40"
                    />
                </div>
            </div>

            <div className="flex flex-col mt-6">
                <div className="-mx-4 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                    <div className="inline-block min-w-full py-2 align-middle md:px-6 lg:px-8">
                        <div className="overflow-hidden border border-gray-200 md:rounded-lg">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th scope="col" className="py-3.5 px-4 text-sm font-normal text-left rtl:text-right text-gray-500">
                                            <button className="flex items-center gap-x-3 focus:outline-none">
                                                <span>Nome</span>
                                                <svg className="h-3" viewBox="0 0 10 11" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M2.13347 0.0999756H2.98516L5.01902 4.79058H3.86226L3.45549 3.79907H1.63772L1.24366 4.79058H0.0996094L2.13347 0.0999756ZM2.54025 1.46012L1.96822 2.92196H3.11227L2.54025 1.46012Z" fill="currentColor" stroke="currentColor" strokeWidth="0.1" />
                                                    <path d="M0.722656 9.60832L3.09974 6.78633H0.811638V5.87109H4.35819V6.78633L2.01925 9.60832H4.43446V10.5617H0.722656V9.60832Z" fill="currentColor" stroke="currentColor" strokeWidth="0.1" />
                                                    <path d="M8.45558 7.25664V7.40664H8.60558H9.66065C9.72481 7.40664 9.74667 7.42274 9.75141 7.42691C9.75148 7.42808 9.75146 7.42993 9.75116 7.43262C9.75001 7.44265 9.74458 7.46304 9.72525 7.49314C9.72522 7.4932 9.72518 7.49326 9.72514 7.49332L7.86959 10.3529L7.86924 10.3534C7.83227 10.4109 7.79863 10.418 7.78568 10.418C7.77272 10.418 7.73908 10.4109 7.70211 10.3534L7.70177 10.3529L5.84621 7.49332C5.84617 7.49325 5.84612 7.49318 5.84608 7.49311C5.82677 7.46302 5.82135 7.44264 5.8202 7.43262C5.81989 7.42993 5.81987 7.42808 5.81994 7.42691C5.82469 7.42274 5.84655 7.40664 5.91071 7.40664H6.96578H7.11578V7.25664V0.633865C7.11578 0.42434 7.29014 0.249976 7.49967 0.249976H8.07169C8.28121 0.249976 8.45558 0.42434 8.45558 0.633865V7.25664Z" fill="currentColor" stroke="currentColor" strokeWidth="0.3" />
                                                </svg>
                                            </button>
                                        </th>
                                        <th scope="col" className="px-12 py-3.5 text-sm font-normal text-left rtl:text-right text-gray-500">
                                            CNPJ
                                        </th>
                                        <th scope="col" className="px-4 py-3.5 text-sm font-normal text-left rtl:text-right text-gray-500">
                                            Email
                                        </th>
                                        <th scope="col" className="px-4 py-3.5 text-sm font-normal text-left rtl:text-right text-gray-500">
                                            Descrição
                                        </th>
                                        <th scope="col" className="relative py-3.5 px-4">
                                            <span className="sr-only">Edit</span>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {fornecedores.map((fornecedor: Fornecedor, idx: number) => (
                                        <tr key={fornecedor.id || idx}>
                                            <td className="px-4 py-4 text-sm font-medium whitespace-nowrap">
                                                <div>
                                                    <h2 className="font-medium text-gray-800">{fornecedor.name}</h2>
                                                </div>
                                            </td>
                                            <td className="px-12 py-4 text-sm font-medium whitespace-nowrap">
                                                <div className="inline px-3 py-1 text-sm font-normal rounded-full text-emerald-500 gap-x-2 bg-emerald-100/60">
                                                    {fornecedor.cnpj}
                                                </div>
                                            </td>
                                            <td className="px-4 py-4 text-sm whitespace-nowrap">
                                                <div>
                                                    <h4 className="text-gray-700">{fornecedor.email}</h4>
                                                </div>
                                            </td>
                                            <td className="px-4 py-4 text-sm whitespace-nowrap">
                                                <div>
                                                    <p className="text-gray-700">{fornecedor.description || ''}</p>
                                                </div>
                                            </td>
                                            <td className="px-4 py-4 text-sm whitespace-nowrap">
                                                <div className="flex items-center relative" ref={dropdownRef}>
                                                    <button 
                                                        onClick={() => setOpenDropdownIndex(openDropdownIndex === idx ? null : idx)} 
                                                        className="px-1 py-1 text-gray-500 transition-colors duration-200 rounded-lg hover:bg-gray-100 cursor-pointer"
                                                    >
                                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
                                                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z" />
                                                        </svg>
                                                        <span className="sr-only">Open options</span>
                                                    </button>
                                                    {openDropdownIndex === idx && (
                                                        <div className="absolute right-0 top-8 z-10 w-48 mt-2 bg-white border border-gray-200 rounded-md shadow-lg">
                                                            <ul className="py-1">
                                                                <li>
                                                                    <button
                                                                        onClick={() => handleEdit(fornecedor)}
                                                                        className="fixed block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer"
                                                                    >
                                                                        Editar
                                                                    </button>
                                                                </li>
                                                                <li>
                                                                    <button
                                                                        onClick={() => fornecedor.id && handleDelete(fornecedor.id)}
                                                                        className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100 cursor-pointer"
                                                                    >
                                                                        Deletar
                                                                    </button>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div className="mt-6 sm:flex sm:items-center sm:justify-between">
                <div className="text-sm text-gray-500">
                    Page <span className="font-medium text-gray-700">1 of 10</span> 
                </div>

                <div className="flex items-center mt-4 gap-x-4 sm:mt-0">
                    <button className="flex items-center justify-center w-1/2 px-5 py-2 text-sm text-gray-700 capitalize transition-colors duration-200 bg-white border rounded-md sm:w-auto gap-x-2 hover:bg-gray-100">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-5 h-5 rtl:-scale-x-100">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M6.75 15.75L3 12m0 0l3.75-3.75M3 12h18" />
                        </svg>
                        <span>Previous</span>
                    </button>

                    <button className="flex items-center justify-center w-1/2 px-5 py-2 text-sm text-gray-700 capitalize transition-colors duration-200 bg-white border rounded-md sm:w-auto gap-x-2 hover:bg-gray-100">
                        <span>Next</span>
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-5 h-5 rtl:-scale-x-100">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M17.25 8.25L21 12m0 0l-3.75 3.75M21 12H3" />
                        </svg>
                    </button>
                </div>
            </div>
            {showImportModal && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-4xl max-h-[80vh] overflow-y-auto relative">
                        <button
                            onClick={closeImportModal}
                            className="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-xl"
                        >
                            ✕
                        </button>
                        
                        <h2 className="text-xl font-semibold text-gray-800 mb-4">
                            Importar Fornecedores via JSON
                        </h2>
                        
                        <div className="mb-4">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Selecionar arquivo JSON:
                            </label>
                            <input
                                ref={fileInputRef}
                                type="file"
                                accept=".json"
                                onChange={handleFileImport}
                                className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="jsonInput" className="block text-sm font-medium text-gray-700 mb-2">
                                Ou cole o JSON aqui:
                            </label>
                            <textarea
                                id="jsonInput"
                                value={jsonText}
                                onChange={(e) => setJsonText(e.target.value)}
                                rows={10}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 font-mono text-sm"
                                placeholder="Cole seu JSON aqui..."
                            />
                        </div>

                        {importError && (
                            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-md">
                                <p className="text-sm text-red-600">{importError}</p>
                            </div>
                        )}

                        <div className="mb-4">
                            <h3 className="text-sm font-medium text-gray-700 mb-2">Exemplo de formato JSON:</h3>
                            <pre className="bg-gray-100 p-3 rounded-md text-xs overflow-x-auto">
                                {exampleJson}
                            </pre>
                        </div>

                        <div className="flex justify-end space-x-3">
                            <button
                                onClick={closeImportModal}
                                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none"
                            >
                                Cancelar
                            </button>
                            <button
                                onClick={handleImportJson}
                                disabled={!jsonText.trim()}
                                className={`px-4 py-2 text-sm font-medium text-white rounded-md focus:outline-none ${
                                    jsonText.trim()
                                        ? 'bg-blue-600 hover:bg-blue-700'
                                        : 'bg-gray-400 cursor-not-allowed'
                                }`}
                            >
                                Importar
                            </button>
                        </div>
                    </div>
                </div>
            )}


            {showForm && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/20 bg-opacity-50">
                    <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-2xl relative">
                        <button
                            onClick={closeForm}
                            className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                        >
                            ✕
                        </button>
                        <AddFornecedoresForm
                            fornecedor={fornecedorEditando}
                            onSave={async (form: Fornecedor) => {
                                try {
                                    const isEdit = fornecedorEditando?.id;
                                    const response = await fetch(
                                        isEdit
                                            ? `http://localhost:8080/Neomind-1.0-SNAPSHOT/api/fornecedores/${fornecedorEditando.id}`
                                            : 'http://localhost:8080/Neomind-1.0-SNAPSHOT/api/fornecedores/',
                                        {
                                            method: isEdit ? 'PUT' : 'POST',
                                            headers: {
                                                'Content-Type': 'application/json',
                                            },
                                            body: JSON.stringify(form),
                                        }
                                    );

                                    if (!response.ok) throw new Error('Erro ao salvar fornecedor');

                                    const savedFornecedor = await response.json();
                                    console.log('savedFornecedor', savedFornecedor);

                                    if (isEdit) {
                                        setFornecedores(prev => prev.map(f =>
                                            f.id === fornecedorEditando.id ? savedFornecedor : f
                                        ));
                                    } else {
                                        setFornecedores(prev => [...prev, savedFornecedor]);
                                    }
                                    
                                    closeForm();
                                } catch (error) {
                                    console.error(error);
                                    alert("Erro ao salvar fornecedor.");
                                }
                            }}
                        />
                    </div>
                </div>
            )}
        </section>
    )
}