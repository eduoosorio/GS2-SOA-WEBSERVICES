## Future Work Hub

Plataforma SOA orientada a serviços, desenvolvida para apoiar profissionais e organizações a navegarem pelo tema **“o futuro do trabalho”**. A aplicação combina gestão de oportunidades resilientes à automação, perfis de capacidades e mentoria personalizada, demonstrando os principais conceitos de Arquitetura Orientada a Serviços e WebServices modernos.

### Integrantes do grupo
- Edudardo Osorio Filho - RM 550161
- Fabio Hideike Kamikihara - RM 550610
> Atualize os nomes acima antes de publicar o repositório.

### Tecnologias principais
- Java 17, Spring Boot 3.4, Maven
- Spring Web, Spring Data JPA, Spring Security, Bean Validation
- MySQL 8 (com `docker-compose.yml` para execução local)
- JWT (Auth0 Java JWT) para autenticação stateless

### Organização por serviços
- `auth`: registro, login e emissão de JWT.
- `opportunity`: CRUD de oportunidades do trabalho do futuro e recomendações inteligentes.
- `profile`: atualização de perfil, VO `ContactInfo`, mentoria e regras de negócio.
- `shared`: DTOs padrões, tratamento global (`@RestControllerAdvice`), segurança e exceções reutilizáveis.

### Critérios de avaliação atendidos
- **Entities/VO/Enums/DTO/Controllers**: `UserAccount`, `WorkOpportunity`, `MentorshipRequest`, `ContactInfo` (Value Object), `RoleType`, `WorkMode`, `MentorshipStatus`, DTOs para autenticação, oportunidades e perfil.
- **ResponseEntity padronizado**: uso de `ApiResponse<T>` em todos os controllers.
- **Tratamento global de exceções**: `GlobalExceptionHandler` com validação e mensagens consistentes.
- **Segurança e autenticação**: Spring Security + JWT stateless, filtro customizado e seeding de usuário admin.
- **Autorização por perfis**: `@PreAuthorize` em endpoints sensíveis e criação automática de `ADMIN`, `MANAGER`, `TALENT`.
- **Política stateless + JWT**: `SecurityConfig` desabilita sessão e adiciona `JwtAuthenticationFilter`.
- **Serviços de negócio**: `AuthService`, `OpportunityService`, `ProfileService` encapsulam regras e casos de uso.
- **Modularização**: pacotes separados por contexto (auth, opportunity, profile, shared).

### Como executar localmente
```bash
# 1. (Opção recomendada) Subir MySQL via Docker
docker compose up -d

# 2. Exportar variáveis conforme o ambiente
set DB_URL=jdbc:mysql://localhost:3306/future_work_hub
set DB_USERNAME=futurework
set DB_PASSWORD=futurework
set JWT_SECRET=sua-chave-secreta

# 3. Rodar a aplicação com MySQL
.\mvnw.cmd spring-boot:run

# (Alternativa sem MySQL instalado) usar o profile local com H2 em memória
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Credenciais padrão após o seed (`DataInitializer`):
- usuário: `admin` ou `admin@futurework.com`
- senha: `admin123`

### Endpoints principais
- `POST /api/auth/register` – cria usuários com perfil TALENT.
- `POST /api/auth/login` – retorna JWT.
- `GET /api/profile/me`, `PUT /api/profile/me`, `POST /api/profile/mentorships` – perfil e mentoria.
- `GET /api/opportunities`, `/api/opportunities/{id}`, `/api/opportunities/recommended` – leitura.
- `POST|PUT /api/opportunities` (MANAGER/ADMIN) e `DELETE /api/opportunities/{id}` (ADMIN) – gestão completa.

### Informações adicionais
- O arquivo `application.yml` já está preparado para uso com MySQL ou variáveis de ambiente.
- As entidades utilizam `ResponseEntity` + `ApiResponse` para respostas consistentes.
- Ajuste os nomes dos integrantes e publique o repositório como **público** no GitHub conforme solicitado.

