# Frontend Angular (microservices e-commerce)

Interface Angular qui consomme l’API Gateway (`http://localhost:8080`) pour lister les produits et créer des commandes.

## Pré-requis
- Node 18+ / npm
- Services back + API Gateway démarrés (ports 8080/8761/8888 par défaut du projet).

## Lancer en dev
```bash
cd frontend
npm install          # déjà fait une fois, à relancer si besoin
npm run start:proxy  # ng serve avec proxy vers le gateway
```
Le proxy `frontend/proxy.conf.json` redirige `/products` et `/orders` vers `http://localhost:8080`.

## Build prod
```bash
npm run build
```
Le bundle est généré dans `frontend/dist/frontend`. À servir derrière le gateway/nginx si besoin.
