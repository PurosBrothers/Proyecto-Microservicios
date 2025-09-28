# 🎨 GUÍA FRONTEND - MANEJO DE IMÁGENES CON VITE + REACT

## ⚛️ Componente React para Subida de Imágenes

### 1. Componente Principal - UserProfileImage.jsx

```jsx
import React, { useState, useEffect } from 'react';
import './UserProfileImage.css';

const UserProfileImage = ({ 
  userId, 
  token, 
  userName = 'Usuario',
  size = 150,
  onImageUploaded = () => {} 
}) => {
  const [imageUrl, setImageUrl] = useState('');
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');

  // Configuración del API
  const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8083';
  const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL || 'http://localhost:8080';

  // Generar URL de imagen por defecto
  const getDefaultAvatar = (name = userName) => {
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=007bff&color=fff&size=${size}`;
  };

  // Cargar imagen actual del usuario
  useEffect(() => {
    if (userId) {
      setImageUrl(`${API_BASE}/images/${userId}`);
    }
  }, [userId]);

  // Manejar subida de archivo
  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // Validar tipo de archivo
    if (!file.type.startsWith('image/')) {
      setError('Por favor selecciona un archivo de imagen válido');
      return;
    }

    // Validar tamaño (5MB)
    if (file.size > 5 * 1024 * 1024) {
      setError('La imagen debe ser menor a 5MB');
      return;
    }

    setUploading(true);
    setError('');

    const formData = new FormData();
    formData.append('file', file);

    try {
      // Usar Gateway en producción, API directa en desarrollo
      const uploadUrl = import.meta.env.PROD 
        ? `${GATEWAY_URL}/api/users/${userId}/image`
        : `${API_BASE}/users/${userId}/image`;

      const response = await fetch(uploadUrl, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: formData
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || 'Error subiendo imagen');
      }

      const result = await response.json();
      const newImageUrl = result.imageUrl + '?t=' + Date.now(); // Cache bust
      setImageUrl(newImageUrl);
      
      // Callback para notificar al componente padre
      onImageUploaded({
        imageUrl: newImageUrl,
        fileName: result.fileName,
        size: result.size
      });
      
    } catch (error) {
      console.error('Error subiendo imagen:', error);
      setError(error.message);
    } finally {
      setUploading(false);
      // Limpiar el input
      event.target.value = '';
    }
  };

  // Manejar error de carga de imagen
  const handleImageError = () => {
    setImageUrl(getDefaultAvatar());
  };

  // Eliminar imagen
  const handleDeleteImage = async () => {
    if (!window.confirm('¿Estás seguro de que quieres eliminar la imagen?')) {
      return;
    }

    try {
      const deleteUrl = import.meta.env.PROD 
        ? `${GATEWAY_URL}/api/users/${userId}/image`
        : `${API_BASE}/users/${userId}/image`;

      const response = await fetch(deleteUrl, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        setImageUrl(getDefaultAvatar());
        onImageUploaded({ imageUrl: null });
      }
    } catch (error) {
      console.error('Error eliminando imagen:', error);
    }
  };

  return (
    <div className="user-profile-image">
      <div 
        className="avatar-container" 
        style={{ width: size, height: size }}
      >
        <img
          src={imageUrl || getDefaultAvatar()}
          alt={`Avatar de ${userName}`}
          className="avatar"
          onError={handleImageError}
        />
        
        {uploading && (
          <div className="loading-overlay">
            <div className="spinner">⏳</div>
            <span>Subiendo...</span>
          </div>
        )}
        
        <input
          type="file"
          accept="image/jpeg,image/jpg,image/png,image/gif,image/webp"
          onChange={handleFileUpload}
          style={{ display: 'none' }}
          id={`image-input-${userId}`}
          disabled={uploading}
        />
        
        <div className="image-actions">
          <button
            className="upload-button"
            onClick={() => document.getElementById(`image-input-${userId}`).click()}
            disabled={uploading}
            title="Subir imagen"
          >
            📷
          </button>
          
          {imageUrl && imageUrl !== getDefaultAvatar() && (
            <button
              className="delete-button"
              onClick={handleDeleteImage}
              disabled={uploading}
              title="Eliminar imagen"
            >
              🗑️
            </button>
          )}
        </div>
      </div>
      
      {error && (
        <div className="error-message">
          <span>❌ {error}</span>
        </div>
      )}
      
      <div className="image-info">
        <small>JPG, PNG, GIF, WEBP - Máx. 5MB</small>
      </div>
    </div>
  );
};

export default UserProfileImage;
```

### 2. Hook personalizado - useImageUpload.js

```jsx
import { useState, useCallback } from 'react';

export const useImageUpload = (baseUrl) => {
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');

  const uploadImage = useCallback(async (userId, file, token) => {
    setUploading(true);
    setError('');

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch(`${baseUrl}/users/${userId}/image`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
        },
        body: formData
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || 'Error subiendo imagen');
      }

      const result = await response.json();
      return result;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setUploading(false);
    }
  }, [baseUrl]);

  const deleteImage = useCallback(async (userId, token) => {
    try {
      const response = await fetch(`${baseUrl}/users/${userId}/image`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      return response.ok;
    } catch (error) {
      setError(error.message);
      return false;
    }
  }, [baseUrl]);

  return {
    uploadImage,
    deleteImage,
    uploading,
    error,
    clearError: () => setError('')
  };
};
```

### 3. Componente de Lista de Usuarios - UsersList.jsx

```jsx
import React, { useState, useEffect } from 'react';
import UserProfileImage from './UserProfileImage';

const UsersList = ({ token }) => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8083';

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await fetch(`${API_BASE}/users`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.ok) {
        const usersData = await response.json();
        setUsers(usersData);
      }
    } catch (error) {
      console.error('Error obteniendo usuarios:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleImageUploaded = (userId, imageData) => {
    setUsers(prevUsers =>
      prevUsers.map(user =>
        user.id === userId
          ? { ...user, fotoUrl: imageData.imageUrl, tieneImagen: !!imageData.imageUrl }
          : user
      )
    );
  };

  if (loading) {
    return <div className="loading">Cargando usuarios...</div>;
  }

  return (
    <div className="users-list">
      <h2>Lista de Usuarios</h2>
      <div className="users-grid">
        {users.map(user => (
          <div key={user.id} className="user-card">
            <UserProfileImage
              userId={user.id}
              token={token}
              userName={user.nombre}
              size={120}
              onImageUploaded={(imageData) => handleImageUploaded(user.id, imageData)}
            />
            
            <div className="user-info">
              <h3>{user.nombre}</h3>
              <p className="user-email">{user.correo}</p>
              <p className="user-description">{user.descripcion}</p>
              
              <div className="user-badges">
                {user.tieneImagen && (
                  <span className="badge badge-success">📷 Con foto</span>
                )}
                <span className={`badge badge-${user.tipoUsuario?.toLowerCase()}`}>
                  {user.tipoUsuario}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UsersList;
```

### 4. Configuración de Variables de Entorno

Crea un archivo `.env` en la raíz de tu proyecto Vite:

```env
# .env
VITE_API_BASE=http://localhost:8083
VITE_GATEWAY_URL=http://localhost:8080
VITE_KEYCLOAK_URL=http://localhost:8081
VITE_KEYCLOAK_REALM=proyect-ms-realm
VITE_KEYCLOAK_CLIENT=user-ms-client
```

### 5. Estilos CSS - UserProfileImage.css

```css
/* UserProfileImage.css */
.user-profile-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

.avatar-container {
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.2s ease;
}

.avatar-container:hover {
  transform: scale(1.02);
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border: 3px solid #007bff;
  border-radius: 50%;
  cursor: pointer;
  transition: opacity 0.3s ease;
}

.avatar:hover {
  opacity: 0.9;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.spinner {
  font-size: 24px;
  animation: spin 1s linear infinite;
}

.loading-overlay span {
  font-size: 12px;
  font-weight: 500;
  color: #007bff;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.image-actions {
  position: absolute;
  bottom: -5px;
  right: -5px;
  display: flex;
  gap: 5px;
}

.upload-button, .delete-button {
  background: #007bff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  cursor: pointer;
  font-size: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-button:hover:not(:disabled) {
  background: #0056b3;
  transform: scale(1.1);
}

.delete-button {
  background: #dc3545;
}

.delete-button:hover:not(:disabled) {
  background: #c82333;
  transform: scale(1.1);
}

.upload-button:disabled, .delete-button:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
}

.error-message {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  text-align: center;
  max-width: 300px;
}

.error-message span {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.image-info {
  text-align: center;
  color: #6c757d;
  font-size: 12px;
  margin-top: 4px;
}

/* Estilos para la lista de usuarios */
.users-list {
  padding: 20px;
}

.users-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.user-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 1px solid #e9ecef;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
}

.user-info {
  margin-top: 16px;
  text-align: center;
}

.user-info h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 18px;
}

.user-email {
  color: #007bff;
  font-size: 14px;
  margin: 4px 0;
}

.user-description {
  color: #666;
  font-size: 14px;
  margin: 8px 0;
  line-height: 1.4;
}

.user-badges {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.badge-success {
  background: #d4edda;
  color: #155724;
}

.badge-cliente {
  background: #cce7ff;
  color: #004085;
}

.badge-proveedor {
  background: #fff3cd;
  color: #856404;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
  font-size: 16px;
}

/* Responsive */
@media (max-width: 768px) {
  .users-grid {
    grid-template-columns: 1fr;
  }
  
  .user-card {
    padding: 16px;
  }
}
```

### 6. Ejemplo de uso en App.jsx

```jsx
import React, { useState, useEffect } from 'react';
import UsersList from './components/UsersList';
import UserProfileImage from './components/UserProfileImage';
import './App.css';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [currentUser, setCurrentUser] = useState(null);

  // Función para obtener el usuario actual
  const getCurrentUser = async () => {
    if (!token) return;
    
    try {
      const response = await fetch('http://localhost:8083/users/me', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      if (response.ok) {
        const user = await response.json();
        setCurrentUser(user);
      }
    } catch (error) {
      console.error('Error obteniendo usuario actual:', error);
    }
  };

  useEffect(() => {
    getCurrentUser();
  }, [token]);

  const handleImageUploaded = (imageData) => {
    console.log('Nueva imagen subida:', imageData);
    // Actualizar el estado del usuario actual si es necesario
    if (currentUser) {
      setCurrentUser({
        ...currentUser,
        fotoUrl: imageData.imageUrl,
        tieneImagen: !!imageData.imageUrl
      });
    }
  };

  if (!token) {
    return (
      <div className="login-prompt">
        <h2>Por favor, inicia sesión</h2>
        <p>Necesitas autenticarte para ver y subir imágenes</p>
      </div>
    );
  }

  return (
    <div className="App">
      <header className="app-header">
        <h1>🏖️ Marketplace Turístico</h1>
        
        {currentUser && (
          <div className="current-user">
            <UserProfileImage
              userId={currentUser.id}
              token={token}
              userName={currentUser.nombre}
              size={60}
              onImageUploaded={handleImageUploaded}
            />
            <span>Hola, {currentUser.nombre}!</span>
          </div>
        )}
      </header>

      <main>
        <UsersList token={token} />
      </main>
    </div>
  );
}

export default App;
```

### 7. Configuración de Vite - vite.config.js

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Proxy para desarrollo - redirige llamadas del API al backend
      '/api': {
        target: 'http://localhost:8080', // Gateway
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api')
      }
    }
  },
  define: {
    // Variables globales disponibles en el frontend
    __API_BASE__: JSON.stringify(process.env.NODE_ENV === 'production' 
      ? 'https://tu-dominio.com' 
      : 'http://localhost:8083'
    )
  }
})
```

### 8. Instalación de dependencias

```bash
# Crear proyecto Vite + React
npm create vite@latest mi-turismo-app -- --template react
cd mi-turismo-app

# Instalar dependencias adicionales si las necesitas
npm install
npm install axios  # opcional, para manejo de HTTP más avanzado
```

## ✅ **Resumen completo para Vite + React:**

### 🚀 **Características implementadas:**
- ✅ **Componente reutilizable** con props configurables
- ✅ **Hook personalizado** para lógica de subida
- ✅ **Variables de entorno** para desarrollo/producción
- ✅ **Proxy de Vite** para evitar CORS en desarrollo
- ✅ **Loading states** y manejo de errores robusto
- ✅ **Fallback automático** a UI-Avatars
- ✅ **Cache busting** con timestamps
- ✅ **Responsive design** con CSS Grid
- ✅ **Accesibilidad** con alt texts y titles

### 🎯 **URLs del API:**
- **Desarrollo**: `http://localhost:8083/images/{userId}`
- **Producción**: `http://localhost:8080/api/users/images/{userId}` (a través del Gateway)
- **Subida**: `POST /users/{userId}/image` con `FormData`
- **Eliminación**: `DELETE /users/{userId}/image`

### 🔧 **Configuración necesaria:**
1. **Variables de entorno** en `.env`
2. **Proxy de Vite** para desarrollo sin CORS
3. **Token JWT** para autenticación
4. **Manejo de estados** con React hooks

### 📦 **Estructura de archivos:**
```
src/
├── components/
│   ├── UserProfileImage.jsx
│   ├── UserProfileImage.css
│   ├── UsersList.jsx
│   └── hooks/
│       └── useImageUpload.js
├── App.jsx
├── App.css
└── .env
```

¡Listo! Ahora tienes todo lo necesario para implementar el sistema de imágenes con Vite + React de forma profesional y escalable. 🎨✨