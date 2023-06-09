# Podstawy Kubernetesa - zarządzanie aplikacjami w klastrze

## 1.Wstęp

<figure align="center">
    <img src="assets/kubernetes-logo.png" style="margin: 0 auto; width: 25%;"/>
</figure>

Każda osoba, która mianuje się programistą niezależnie od swojego doświadczenia spotkał się z pojęciem tzw. aplikacji monolitycznej, czyli aplikacji, w której cała logika i zasoby są skoncentrowane w jednym miejscu. O ile sama zasada jej tworzenia i działania jest dosyć prosta wraz z rozwojem technologii i rosnącym zapotrzebowaniem na skalowalność i niezawodność, taki typ aplikacji zaczął stanowić problem. Skupienie wszystkich zasobów i funkcjonalności w jednym miejscu skutkowało tym, że każdy najmniejszy jej problem oddziaływał automatycznie na całą jej instancję, co utrudniało rozwiązanie problemów i prowadziło do niewydajności.

Dlatego też, na światło dzienne przyszła nowa architektura. Mikroserwisy, bo tak się ona nazwa to podejście do tworzenia aplikacji, w którym system jest podzielony na mniejsze, autonomiczne usługi, które komunikują się ze sobą za pomocą lekkich protokołów. Zwykle każdy mikroserwis odpowiada za konkretną funkcjonalność, co ułatwia skalowanie i utrzymanie systemu. Mikroserwisy umożliwiają również elastyczne wdrażanie i aktualizację poszczególnych komponentów, niezależnie od reszty systemu. Dzięki temu, nawet jeśli wystąpi problem w jednej części, nie wpływa on negatywnie na całą aplikację, a jedynie na odpowiednią funkcjonalność.

W kontekście mikroserwisów, nie obyło się od zapotrzebowania na narzędzie pozwalające na elastyczną i skalowalną platformę do zarządzania tymi częściami. Dlatego, też pojawił się tzw. Kubernetes, który umożliwiał wieloplatformowe uruchamianie, monitorowanie i skalowanie wielu instancji mikroserwisów.

Oczywiście, przed zagłębieniem się w szczegóły samego Kubernetesa, warto zrozumieć trzy kluczowe pojęcia, które stanowią fundament tej platformy: **pod**, **node** i **cluster**. Ale zanim do nich dojdziemy wcześniej jednak warto przede wszystkim zagłębić się w jeszcze jedno pojęcie tzw. **kontenera**. Samo pojęcie może nie jest czysto związane z Kubernetes’em, a głównie Docker'em, ale jest ściśle powiązane z samą konteneryzacją, na której działa Kubernetes.

Poznanie wszystkich czterech pojęć pozwoli na szerszą perspektywe i spojerzenie na to, w jaki Kubernetes organizuje i zarządza naszymi aplikacjami w środowisku kontenerowym.

### 1.1. Kontener – izolowany środowisko uruchomieniowe

<figure align="center" style="margin: 0 auto; width: 25%; aspect-ratio: 1 / 1">
    <img src="assets/docker-container.png"/>
</figure>

**Kontener** jest izolowanym środowiskiem uruchomieniowym, które zawiera wszystko, czego nasza aplikacja potrzebuje do działania, takie jak kod, zależności, narzędzia systemowe i konfiguracje. Można go porównać do pudełka, które pakuje i izoluje naszą aplikację oraz jej zależności od systemu operacyjnego i innych aplikacji.

Kontenery umożliwiają nam przenośność aplikacji, ponieważ możemy je uruchamiać na różnych platformach i środowiskach, zachowując ich spójność i niezawodność.

### 1.2. Pod - najmniejsza jednostka w Kubernetes

<figure align="center">
    <img src="assets/architecture-pod.png" style="margin: 0 auto; width: 200px;"/>
</figure>

**Pod** jest najmniejszą jednostką w Kubernetes, w której możemy uruchamiać nasze aplikacje. Można go traktować jako logiczne pojemniki, które zawierają jedną lub wiele powiązanych ze sobą kontenerów. W kontekście aplikacji możemy sobie to wyobrazić jako pojedynczą instancję konkretnej aplikacji, która działa w izolowanym środowisku.

Pod zapewnia środowisko uruchomieniowe dla naszej aplikacji, wraz z zasobami, takimi jak pamięć, procesor, sieć, itp. Każdy pod ma unikalny adres IP wewnątrz klastra, o którym później, co umożliwia komunikację z innymi podami.

### 1.3. Node - fizyczny lub wirtualny host

<figure align="center">
    <img src="assets/architecture-node.png" style="margin: 0 auto; width: 250px;"/>
</figure>

**Node** (węzeł) jest fizycznym lub wirtualnym hostem w zbiorze węzłów Kubernetesa, na którym uruchamiane są pody. Możemy to sobie wyobrazić jako komputer, na którym działa nasz Kubernetes. Node dostarcza zasoby obliczeniowe, takie jak pamięć, procesor, przestrzeń dyskową, itp., które są wykorzystywane przez pody.

W kontekście naszej aplikacji, węzły mogą być porównane do serwerów, na których uruchamiane są jej instancje.

### 1.4. Cluster - zbiór węzłów

<figure align="center">
    <img src="assets/architecture-cluster.png" style="margin: 0 auto;  width: 500px;"/>
</figure>

**Cluster** (klaster) to zbiór węzłów, które działają razem jako jedna jednostka. Klastry Kubernetesa są zorganizowane hierarchicznie i składają się z jednego lub wielu węzłów. Każdy klaster ma swoje unikalne identyfikatory i może zawierać wiele aplikacji uruchomionych w postaci podów na różnych węzłach.

Klastry zapewniają zarządzanie i kontrolę nad naszymi aplikacjami w Kubernetes. Mogą automatycznie uruchamiać nowe pody, skalować aplikacje w odpowiedzi na obciążenie oraz zapewniać odporność na awarie poprzez migrację podów na inne węzły w przypadku awarii.

Właśnie te cztery pojęcia – Kontener, Pod, Node i Cluster - stanowią fundamenty zrozumienia Kubernetesa. Posiadając lepsze zrozumienie ich roli i związku, można przejść dalej i poznać bardziej zaawansowane aspekty Kubernetesa oraz sposób, w jaki można go wykorzystać do wdrażania i zarządzania aplikacjami opartymi na np. Spring Boot.

## 2. Komponenty Kubernetesa

W kontekście zarządzania aplikacjami w Kubernetesie istnieje kilka kluczowych komponentów, które odgrywają istotną rolę w procesie wdrażania i zarządzania aplikacjami. W tym rozdziale przyjrzymy się trzem ważnym komponentom:

* ReplicationController
* Service
* Deployment.

### 2.1. ReplicationController - zarządzanie replikami

**ReplicationController** (kontroler replik) jest komponentem, który odpowiada za utrzymanie określonej liczby replik (tj. kopi) podów w klastrze. Określamy pożądaną liczbę replik, a ReplicationController dba o to, żeby ta liczba była utrzymywana, niezależnie od awarii lub zmian w klastrze.

W kontekście naszej aplikacji, ReplicationController jest szczególnie przydatny, gdy chcemy uruchamiać i utrzymywać wiele jej instancji zapewniając skalowalność i wysoką dostępność. Komponent ten może automatycznie tworzyć nowe repliki podów, jeśli któreś z nich ulegną awarii lub gdy obciążenie wzrasta.

### 2.2. Service - dostęp do replik

<figure align="center">
    <img src="assets/component-service.png" style="margin: 0 auto;  width: 250px;"/>
</figure>

**Service** (usługa) jest komponentem, który umożliwia dostęp do replik podów w klastrze. Service definiuje stały punkt dostępu i mechanizm bilansowania obciążenia, dzięki czemu aplikacje zewnętrzne lub inne pody w klastrze mogą komunikować się z replikami aplikacji.

W Kubernetesie istnieje kilka typów usług, które można zdefiniować w zależności od potrzeb aplikacji:

- **ClusterIP** - jest to domyślny typ usługi. Usługa ta przypisuje wewnętrzny, stały adres IP i nazwę domenową w obrębie klastra. Możemy jej użyć, gdy chcemy umożliwić dostęp do naszej aplikacji tylko wewnątrz klastra, dla innych podów lub usług w klastrze.
- **ExternalName** - umożliwia mapowanie zasobów w klastrze na zewnętrzne nazwy domenowe. Jest to przydatne, gdy chcemy skonfigurować dostęp do zewnętrznego serwisu, który nie jest uruchomiony wewnątrz klastra, dzięki czemu możemy używać nazwy domenowej w celu uzyskania dostępu do zasobu spoza klastra.
- **LoadBalancer** - automatycznie tworzy zewnętrzny punkt dostępu i równoważy obciążenie ruchu na wybrane repliki.
- **NodePort** - przypisuje stały port na każdym węźle klastra, który przekierowuje ruch do replik, dzięki czemu możemy uzyskać dostęp do replik spoza klastra, korzystając z adresu IP węzła klastra i przypisanego portu. Jest to proste i najczęściej wykorzystywane rozwiązanie, które można wykorzystać do łatwego dostępu do aplikacji w celach rozwoju i testowania.

W przypadku naszej aplikacji, Service może zapewnić stały adres IP i nazwę domenową, które są niezależne od zmian w klastrze. Dzięki temu inne komponenty systemu mogą łatwo odnaleźć i komunikować się z naszą aplikacją, niezależnie od tego, na którym węźle i w którym podzie aktualnie się znajduje. Wybór konkretnego typu usługi zależy od wymagań i kontekstu naszej aplikacji.

### 2.3. Deployment - zarządzanie wersjami aplikacji

<figure align="center">
    <img src="assets/component-deployment.png" style="margin: 0 auto;  width: 350px;"/>
</figure>

**Deployment** (wdrażanie) to komponent, który pozwala na zarządzanie wersjami aplikacji i łatwe wprowadzanie zmian. Deployment definiuje pożądaną konfigurację aplikacji, taką jak liczba replik, obrazy kontenerów i strategia aktualizacji.

W przypadku naszej aplikacji, Deployment umożliwia nam kontrolowane wdrażanie nowych jej wersji, zarządzanie zmianami i skalowalnością. Możemy definiować strategie aktualizacji, takie jak stopniowe wdrażanie (rolling update) lub strategie zero-downtime, aby minimalizować wpływ zmian na działanie aplikacji.

Podsumowując rozdział i powyższe informacje można streścić wszystko do stwierdzenia, że dzięki ReplicationController, Service i Deployment możemy efektywnie zarządzać naszymi aplikacjami w Kubernetesie, gdzie:
- ReplicationController zapewnia skalowalność i wysoką dostępność,
- Service umożliwia komunikację z replikami podów
- Deployment umożliwia łatwe zarządzanie wersjami aplikacji i wdrażanie zmian.

Oczywiście należy pamiętać, że takich komponentów jest o wiele więcej, ale trzy powyższe są zwykle minimalnymi elementami do skutecznego wdrożenia i zarządzania aplikacjami w Kubernetesie.

## 3. Przygotowanie środowiska

Aby móc tworzyć i zarządzać najprostszym klastrem Kubernetesa w pierwszej kolejności potrzebujemy, aby nasza jednostka posiadała zainstalowane trzy główne narzędzia/programy:

- Docker v20.10.20 - https://www.docker.com/products/docker-desktop
- Minikube v1.30.1 - https://minikube.sigs.k8s.io/docs/start
- kubectl v1.27.1 - https://kubernetes.io/docs/tasks/tools/

### 3.1. Docker - platforma kontenerowa

<figure align="center">
    <img src="assets/docker-logo.png" style="margin: 0 auto; width: 25%;"/>
</figure>

**Docker** jest platformą umożliwiającą na pakowanie, dostarczanie i uruchamianie aplikacji w izolowanych kontenerach pozwalając na prace niezależnie od systemu operacyjnego, czy środowiska.

Instrukcje instalacji:

1. Przejdź do strony oficjalnej Dockera: https://www.docker.com/products/docker-desktop
2. Pobierz instalator Dockera dla systemu Windows.
3. Uruchom pobrany instalator i postępujemy zgodnie z instrukcjami.

<figure align="center">
    <img src="assets/docker-website.png"/>
</figure>

Instalacja Dockera w dużej mierze jest bardzo prosta i polega na kliknięciu kolejno przycisków `Ok` i `Close`.

Jednakowoż pomimo ogólnej prostoty instalacji, warto tutaj zaznaczyć, że Docker jak i sama wirtualizacja ze względu na swoją specyfikę wymagają obecności oprogramowania zwanego Hyper-V, a ten niestety jest dostarczany tylko w konkretnych wersjach systemu Windows (tj. Professional i Enterprise). Użytkownicy, których wersja nie wspiera owego oprogramowania muszą zainstalować sobie tzw. Docker Toolbox, który symuluje wirtualizacje, a działa w ten sposób, że instaluje nam VirtualBox, który zastąpi Hyper-V. W przypadku poradnika/instrukcji użyto Windows 10 Pro zatem wykorzystano Hyper-V.

W przypadku domyślnie wyłączonego Hyper-V, oprogramowanie to można włączyć poprzez _Panel Sterowania / Programy i funkcje / Włącz i wyłącz funkcje systemu Windows_.

<figure align="center">
    <img src="assets/windows-windows-functions.png"/>
</figure>

Po zakończeniu instalacji warto się upewnić, czy proces instalacji przebieg pomyślnie i bez błędów poprzez uruchomienie aplikacji Docker Desktop i wykonanie w systemowych wierszu poleceń CMD polecenia:

```bash
docker version
```

Otrzymanie informacji o kliencie i serwerze, utwierdzi nas w przekonaniu, że wszystko działa proprawnie.
Oczywiście względu na to, że szczegółowa wiedza na temat wyświetlanych informacji nie jest na ten moment potrzebna, dlatego też nie będziemy się im bardziej przyglądać. 

<figure align="center">
    <img src="assets/cmd-docker-version.png"/>
</figure>

Po zainstalowaniu Dockera będziemy mogli tworzyć i uruchamiać kontenery w naszym klastrze Kubernetesa.

### 3.2. Minikube - lokalny klaster Kubernetesa

**Minikube** to narzędzie, które umożliwia uruchomienie lokalnego klastra Kubernetesa na naszym komputerze. Dzięki temu będziemy mogli użyć oferowane przez narzędzi usługi do celów rozwojowych i testowych.

Instrukcje instalacji:

1. Przejdź do strony oficjalnej Minikube: https://minikube.sigs.k8s.io/docs/start
2. Zaznacz odpowiednie pola zgodne dla docelowego systemu Windows.
3. Znajdź sekcje "Installation" i wybierz link z treści punktu 1 znalezionej sekcji.
4. Uruchom pobrany plik instalacyjny i postępujemy zgodnie z instrukcjami (tj. w trakcie podawania ścieżki instalacji warto wybrać taką, która jest łatwa do zapamiętania np. **C:/kubernetes**, co pozwoli nam łatwo ją zapamiętać i później znaleźć)

<figure align="center">
    <img src="assets/minikube-website.png"/>
</figure>

<figure align="center">
    <img src="assets/minikube-website-2.png"/>
</figure>

Jeśli chodzi o sam instalator Minikube'a to poza zmianą ścieżki, kroki postępowania wyglądają identycznie jak w przypadku standardowych instalacji programów, jakie robimy dotychczas na systemie Windows, gdzie naszym jedynym zadaniem jest głównie przeklikliwanie kolejnych przycisków następnego kroku `Next`.

Po zakończeniu instalacji tak samo jak ostatnio warto by było się upewnić, czy proces instalacji przebieg pomyślnie.

W tym przypadku jednak zanim to zrobimy musimy w między czasie zrobić jeszcze jedną rzecz. Otóż, żeby system mógł widzieć minikube w wierszu poleceń CMD i nie tylko, należy pokazać mu gdzie znajdują się odpowiednie pliki i odwołania. Dlatego, też za pomocą Win+R i poniższemu poleceniu uruchamiamy _Właściwości systemu_:

```bash
sysdm.cpl
```

<figure align="center">
    <img src="assets/windows-environment-variables.png" style="margin: 0 auto; width: 60%"/>
</figure>

Klikamy kartę _Zaawansowane_, a następnie _Zmienne środowiskowe_ i w jednej z pokazanych sekcji w zmiennej _Path_ dodajemy ścieżkę (tj. Edytuj -> Nowy) na której zainstalowaliśmy minikube. W moim przypadku jest to **C:/kubernetes** i podwójnie zatwierdzamy OK.

<figure align="center">
    <img src="assets/windows-sysdm.cpl.png" style="margin: 0 auto; width: 70%"/>
</figure>

Teraz po tych czynnościach możemy sprawdzić pomyślność instalacji, poprzez wykonanie polecenia w wierszu poleceń CMD, który warto uruchomić ponownie w celu utrwalenia nowych zmian:

```bash
minikube version
```

Otrzymanie poprawnej odpowiedzi, bez jakiekolwiek błędu będzie oznaczać, że wszystko jest w porządku.

<figure align="center">
    <img src="assets/cmd-minikube-version.png"/>
</figure>

Kolejnym krokiem, jaki musimy podjąć to dostarczenie konfiguracji dla Minikube poprzez wykonanie polecenia:

```bash
minikube start
```

<figure align="center">
    <img src="assets/cmd-minikube-start.png"/>
</figure>

Polecenie to ze względu na swoją specyfikę będzie się wykonywała przez dłuższą chwilę. Czas ten wynika z tego, że Minikube pobiera wszystkie obrazy do Docker'a, które są niezbędne do zbudowania środowiska. Po zakończeniu operacji warto jeszcze wykonać polecenie, które ogólnie jest opcjonalne, ale każda nowa wiedza jest dobra:

```bash
minikube status
```

Dzięki temu poleceniu dowiemy się które usługi Minikube'a działają poprawnie, a które nie.

<figure align="center">
    <img src="assets/cmd-minikube-status.png"/>
</figure>

Informacje uzyskane podczas wykonania powyższego polecenia to m.in:
- _type: Control Plane_ - informuje, że aktualnie uruchomiona jest wersja kontrolera (control plane) klastra Kubernetes, odpowiedzialna za zarządzanie, planowanie i nadzorowanie wdrożeń aplikacji oraz zarządzanie stanem klastra.
- _host: Running_ - informuje, że host, na którym działa Minikube, jest uruchomiony i dostępny.
- _kubelet: Running_ - informuje, że kubelet, który jest agentem odpowiedzialnym za komunikację z Control Plane i zarządzanie kontenerami w węzłach klastra, jest uruchomiony.
- _apiserver: Running_ - informuje, że serwer API klastra Kubernetes (apiserver), który udostępnia interfejs programistyczny dla zarządzania klastrami, jest uruchomiony.
- _kubeconfig: Configured_ - informuje, że skonfigurowano plik kubeconfig, który zawiera dane uwierzytelniające i informacje o klastrze potrzebne do komunikacji z Minikube.

Poprawne wykonanie powyższego polecenia skutkuje tym, że w Dockerze zostaje utworzony nowy kontener minikube, wraz z potrzebnym obrazem i woluminem, który można m.in zobaczyć uruchamiając aplikacje Docker Desktop.

Instalacja Minikube umożliwi nam uruchamianie lokalnego klastra Kubernetesa i testowania naszych aplikacji w środowisku kontenerowym.

### 3.3. kubectl - interfejs wiersza poleceń do Kubernetesa

**kubectl** to narzędzie wiersza poleceń, które umożliwia nam interakcję z klastrami Kubernetesa. Za jego pomocą możemy zarządzać naszymi aplikacjami, replikami, usługami i innymi zasobami w Kubernetesa.

Instrukcje instalacji:

1. Przejdź do strony oficjalnej Kubernetesa: https://kubernetes.io/docs/tasks/tools/
2. Wybierz instalację dla systemu Windows.
3. Znajdź sekcję dotyczącą kubectl i z sekcji "Install kubectl binary with curl on Windows" wybierz link z treści punktu 1.
4. Zapisz plik .exe do folderu, gdzie zainstalowany jest Minikube (tj. w moim przypadku jest to **C:/kubernetes**).

<figure align="center">
    <img src="assets/kubectl-website.png"/>
</figure>

<figure align="center">
    <img src="assets/kubectl-website-2.png"/>
</figure>

Dla dopełnienia tutaj również wywołamy polecenie w wierszu poleceń CMD umożliwiające sprawdzenie poprawności instalacji:

```bash
kubectl version
```

<figure align="center">
    <img src="assets/cmd-kubectl-version.png"/>
</figure>

Jak można zauważyć zwrócone informacje bardziej przypominają błąd niż poprawną odpowiedź, lecz w tym przypadku zależało nam na sprawdzeniu czy wykonywane polecenie zadziała niż na poprawności, czy sposobie odpowiedzi.

Po zakończeniu pobierania kubectl będziemy mogli wykorzystać go do zarządzania naszym klastrem Kubernetesa z poziomu wiersza poleceń.

## 4. Pierwszy klaster i podstawowe komendy

Kubernetes tak samo jak Docker posiada polecenia, które umożliwiają zarządzanie klastrami i podami. Lista takich poleceń jest długa, lecz jeśli chodzi o te najważniejsze, to można do nich zaliczyć te przedstawione poniżej.

### 4.1. Sprawdzenie informacji o klastrze

Aby sprawdzić informacje o swoim klastrze Kubernetesa, należy wykonać polecenie:

```bash
kubectl cluster-info
```

Polecenie to wyświetli podstawowe informacje o klastrze, takie jak adresy API serwera i wersję Kubernetesa. Sprawdzanie tych informacji jest przydatne, aby upewnić się, że klaster działa poprawnie.

<figure align="center">
    <img src="assets/cmd-kubectl-cluster-info.png"/>
</figure>

### 4.2. Pobranie listy węzłów

Aby uzyskać listę węzłów (nodów) w Twoim klastrze, należy użyć polecenia:

```bash
kubectl get node
```

Wykonanie tej komendy wyświetli informacje o węzłach, takie jak nazwa, status, jego rolę, czy wersję. Dzięki temu można łatwo monitorować i zarządzać dostępnymi węzłami w klastrze.

<figure align="center">
    <img src="assets/cmd-kubectl-get-node.png"/>
</figure>

### 4.3. Pobranie listy podów

Aby uzyskać listę podów w Twoim klastrze, należy użyć polecenia:

```bash
kubectl get pods
```

To polecenie wyświetli informacje o podach, takie jak nazwa, status, adres IP i węzeł, na którym pod jest uruchomiony. Dzięki temu możesz monitorować i zarządzać podami w swoim klastrze.

<figure align="center">
    <img src="assets/cmd-kubectl-get-pods.png"/>
    <figcaption>Efekt wykonania polecenia "kubectl get pods"</figcaption>
</figure>

Oczywiście jak można zauważyć w przypadku takiej kolejności wykonywania poleceń nasz Kubernetes nie posiada żadnego pod'a. Oczywiście stan ten zmieni się po użyciu następnego polecenia.

### 4.4. Tworzenie wdrożenia (deployment)

Aby utworzyć nowe wdrożenie (deployment) w Kubernetesa, a zarazem zmienić stan ostatniego polecenia, należy użyć polecenia:

```bash
kubectl create deployment jakas-nazwa --image=kartven/k8s-guide-app:1.0 --port=8080
```

To polecenie tworzy tzw. wdrożenie o nazwie "jakas-nazwa" i umożliwia uruchomienie aplikacji w klastrze.
Jak można zauważyć oprócz nazwy należało również przekazać opcje `image` określającą obraz, z którego będziemy zaciągać informacje i na którego podstawie tworzymy wdrożenie.

Aby zaoszczędzić czas na potrzeby instrukcji stworzono prosty obraz aplikacji napisanej w Spring Boot o nazwie "k8s-guide-app", zawierający informacje o nazwie, instancji i podstawowy licznik wywołań.

Warto, też wspomnieć o tym, że oprócz obrazu przekazujemy również `port` na której jest uruchamiana powyższa aplikacja.

<figure align="center">
    <img src="assets/cmd-kubectl-create-deployment.png"/>
</figure>

### 4.5. Pobranie listy wdrożeń

Aby uzyskać listę wdrożeń w Twoim klastrze, należy użyć polecenia:

```bash
kubectl get deployment
```

To polecenie wyświetli informacje o wdrożeniach, takie jak nazwa, czy żądane i aktualne repliki. Możesz tym samym monitorować i zarządzać wdrożeniami w klastrze.

<figure align="center">
    <img src="assets/cmd-kubectl-get-deployment.png"/>
</figure>

Jak można zauważyć podczas pierwszego użycia powyższego polecenia nasze wdrożenie posiadało pod kolumną `READY` wartość 0/1, co w głównej mierze oznacza, że aplikacja `k8s-guide-app` posiada 1 replikę (tj. kopię), ale do 37 sekundy nie została jeszcze w pełni uruchomiona. 

### 4.6. Wyświetlanie szczegółowych informacji o wdrożeniu

Aby uzyskać szczegółowe informacje o konkretnym wdrożeniu, należy użyć polecenia:

```bash
kubectl describe deployment jakas-nazwa
```

Polecenie to wyświetli informacje, takie jak nazwa wdrożenia, strategia aktualizacji, czy status replik.

<figure align="center">
    <img src="assets/cmd-kubectl-describe-deployment.png"/>
</figure>

Oczywiście jak nie trudno zauwazyć ilość informacji uzyskanej podczas wykonania powyższego polecenia jest tak duża, że nie sposób opisać, jednakowoż na obecną chwilę wystarczy nam informacja o tym, że dzięki niemu można obejrzeć bardziej szczegółowe informacje o swoim wdrożeniu.

### 4.7. Wyświetlanie dzienników (logów) z podu

Aby wyświetlić dzienniki (logi) z konkretnego podu, należy użyć kolejno poleceń:

```bash
kubectl get pods
```


```bash
kubectl logs <jedna-z-nazw-z-poprzedniego-polecenia>
```

Polecenie to wyświetla logi związane z działaniem podu, dzięku niemu można monitorować działanie swojej aplikacji i przeprowadzać analizę dzienników (tj. logów).

<figure align="center">
    <img src="assets/cmd-kubectl-logs-pod.png"/>
</figure>

Jak można zauważyć uzyskanie logów konkretnego poda wymaga wcześniejszego użycia polecenia `kubectl get pods`, co wynika głównie z tego, że możliwość posiadania wielu pod'ów skutkuje tym, że wymaga się unikalności nazw podów, co uniemożliwiający na powtórzenie się tej samej nazwy dla kilku podów w jednym wdrożeniu.

### 4.8. Tworzenie usługi (service)

Każdy kontener posiada taką właściwość, że podstawowo jedyną rzeczą, którą możemy zrobić to zobaczyć co robi uruchomiona na nim aplikacje, lecz samo w sobie nie mamy do niego jawnego dostepu. Sytuacja ta wynika z tego, że na ten moment nie posiadamy jeszcze zdefiniowanego dla niego serwisu, który by to umożliwił.

Aby utworzyć serwis dla wdrożenia o nazwie "jakas-nazwa", można użyć polecenia:

```bash
kubectl expose deployment jakas-nazwa --type=NodePort
```

Polecenie to tworzy serwis, która umożliwi dostęp do wdrożenia spoza klastra. Podczas użycia powyższego polecenia wymagane jest podanie jednego z typów, które omówiono we Wstępie, jeśli nie chce się korzystać z tego domyślnego. Na ten moment jednak użyto "NodePort", ze wzgledu na jego specyfikę działania.

<figure align="center">
    <img src="assets/cmd-kubectl-expose-deployment.png"/>
</figure>

### 4.9. Pobranie listy serwisów

Aby uzyskać listę serwisów w Twoim klastrze, należy użyć polecenia:

```bash
kubectl get services
```

Polecenie to wyświetla informacje o usługach, głównie takie jak nazwa, typ, czy porty na których działa dany serwis. Dzięki temu poleceniu można monitorować i zarządzać usługami w danym klastrze.

<figure align="center">
    <img src="assets/cmd-kubectl-get-services.png"/>
</figure>

Informacja, na którą warto zwrócić uwagę jest `CLUSTER-IP` i `PORT`, czyli adres ip z portem na którym wystawiony dany serwis. Niestety pomimo przyjaznego ich wyglądu, obie te rzeczy są zdefiniowane wewnątrz klastra, więc z poziomu systemu operacyjnego w żaden sposób nie można ich wykorzystać.

Jednakowoż będzie można to zmienić wykorzystując następne polecenie.

### 4.10. Uzyskanie adresu URL usługi

Zdefiniowanie serwisu w klastrze ma taką zaletę, że umożliwia stworzenie tunelowania (tj. techniki, która umożliwia przekazywanie danych między dwoma sieciami lub komputerami poprzez inną sieć, która znajduje się pośrodku) do klastra pozwalającego na dostęp do aplikacji z poziomu systemu operacyjnego. Na ten przykład najlepszym rozwiązaniem będzie uzyskanie adresu URL, które bedzie można wykorzystać w przeglądarce.

 Aby uzyskać taki adres dla serwisu "jakas-nazwa" w klastrze Minikube, należy użyć polecenia dostępnego wyłącznie z poziomu Minikube:

```bash
minikube service jakas-nazwa --url
```

To polecenie umożliwi stworzenie takiego tunelu i na koniec wyświetli adres URL, który będzie można użyć w przeglądarce.

<figure align="center">
    <img src="assets/cmd-kubectl-service-url.png"/>
</figure>

Jak można zauważyć naszą aplikację możemy zobaczyć przechodząc pod adres http://127.0.0.1:64278 w systemowej przegladarce (tj. Google Chrome).

Niestety ze względu na to, że sterownik docker'a dla Windowsa, który umożliwia uruchamianie kontenerów Docker'a na systemie, wymaga uruchomienia procesu w tle powoduje, że wiersz poleceń CMD, który pozwala na dostęp do aplikacji pod adresem URL, musi pozostawać w tym czasie ciągle uruchomiony.

<figure align="center">
    <img src="assets/localhost-app.png"/>
</figure>

## 5. Dashboard - Monitorowanie klastra poprzez GUI

Minikube wraz z możliwością postawienia lokalnie pojedynczego klastra Kubernetesa, dodatkowo dla testów dostarcza możliwość monitorowania i zarządzania klastrami poprzez interfejs graficzny, zwany Kubernetes Dashboard. Interfejs ten to intuicyjne narzędzie, które umożliwia manipulację klastrem i jego wnętrzem w sposób wygodny dla oka i bez konieczności znajomości wszystkich, poza podstawowych poleceń.

### 5.1. Uruchamianie Kubernetes Dashboard

Aby uruchomić Kubernetes Dashboard należy przede wszystkim upewnić się, że Minikube jest uruchomiony i połączony z Twoim klastrem Kubernetesa, poprzez użycie polecenia:

```bash
minikube status
```

<figure align="center">
    <img src="assets/cmd-minikube-status-once.png"/>
</figure>

W przypadku wystąpienia informacji o braku możliwości odnalezienia profilu `minikube` należy użyć omawianego w Rozdziale 4. polecenia `minikube start`.

W momencie kiedy poprzednie polecenie wykonało się poprawnie i bez jakiekolwiek błędów możemy uruchomić dashboard używając polecenia:

```bash
minikube dashboard
```

<figure align="center">
    <img src="assets/cmd-minikube-dashboard.png"/>
</figure>

Polecenie to otworzy przeglądarkę i uruchomi Kubernetes Dashboard w lokalnym środowisku. Niestety jak można zauważyć identycznie jak w przypadku dostępu do aplikacji `k8s-guide-app` możliwość korzystania z interfejsu graficznego równa się konieczności trzymania uruchomionego wiersza poleceń CMD do samego końca.

### 5.2. Korzystanie z interfejsu graficznego

<figure align="center">
    <img src="assets/localhost-dashboard.png"/>
</figure>

Uruchomienie Kubernetes Dashboard, pozwala na dostęp do intuicyjnego interfejsu użytkownika, który umożliwia monitorowanie i zarządzanie klastrami Kubernetesa.

Podstawowymi funkcjami interfejsu są:

1. Przegląd klastra - podstawowe informacje o klastrze, takie jak węzły, podmioty, wdrożenia itp. pozwalającej na szybką ocenę stanu klastra i sprawdzenie, czy wszystko działa prawidłowo.
2. Przegląd podów - umożliwia zobaczenie listy wszystkich podów w klastrze, a następnie sprawdzenie ich stanów, statusów, adresów IP, itd.
3. Przegląd usług - umożliwia zobacznie listy wszystkich usług w klastrze. Pozwala na sprawdzenie, jakie usługi są dostępne, jakie porty im przypisane, a także ich typy i wiele innych informacji.
4. Zarządzanie wdrożeniami - umożliwia zarządzanie wdrożeniami aplikacji w klastrze. Pozwala tworzyć nowe wdrożenia, aktualizować istniejące, skalować je w górę lub w dół oraz usuwać niepotrzebne wdrożenia.
5. Monitorowanie zasobów - umożliwia monitorowanie wykorzystania zasobów w klastrze, takich jak CPU, pamięć, dysk czy sieć. Pozwala śledzić i analizować, jak Twoje aplikacje wykorzystują poszczególne zasoby.

## 6. Dalsze kroki i możliwości

Po opanowaniu podstaw tworzenia klastra Kubernetesa, istnieje wiele innych rzeczy, które można zrobić. Oto kilka sugestii:

1. **Skalowanie aplikacji** - wypróbuj prostą metodę skalowania aplikacji w Kubernetesa, taką jak jak zwiększanie i zmniejszanie replikacji, aby zmienić liczbę instancji aplikacji.
2. **Zarządzanie konfiguracją** - poznaj podstawy zarządzania konfiguracją w Kubernetesa, takie jak tworzenie ConfigMaps do przechowywania ustawień aplikacji.
3. **Przełączanie wersji aplikacji** - wykorzystaj prostą strategię wdrażania w Kubernetesa, aby płynnie przełączać się między różnymi wersjami aplikacji.
4. **Monitorowanie i dzienniki** - skonfiguruj podstawowe monitorowanie aplikacji w Kubernetesa, takie jak zbieranie metryk i dzienników aplikacji.
5. **Eksperymenty z siecią** - przetestuj podstawowe modele sieciowe w Kubernetesa, takie jak komunikacja między usługami w tej samej podsieci.
6. **Zabezpieczenia i zarządzanie tożsamością** - poznaj podstawy zabezpieczeń Kubernetesa, takie jak zarządzanie dostępem do zasobów klastra na podstawie ról i uprawnień.
7. **CI/CD** - zintegruj podstawową automatyzację CI/CD z Kubernetesem, taką jak automatyczne wdrażanie aplikacji na klastrze.

Te sugestie umożliwiają wstęp do dalszej nauki i eksploracji Kubernetes, poprzez zgłębianie tematu, eksperymentowanie i rozwijanie swojej wiedzy.

## 7. Literatura

Do stworzenia tej instrukcji korzystano z różnych źródeł informacji na temat Kubernetesa i Minikube'a, ale przede wszystkim:

- [Oficjalna dokumentacja Kubernetesa](https://kubernetes.io/docs/) - oficjalna dokumentacja Kubernetesa
- [Oficjalna dokumentacja Minikube](https://minikube.sigs.k8s.io/docs/) - oficjalna dokumentacja Minikube
- [Kubernetes in Action](https://www.manning.com/books/kubernetes-in-action) - autorstwa Marko Luksa
- [Kubernetes: Up and Running](https://www.oreilly.com/library/view/kubernetes-up-and/9781492046510/) - autorstwa Brendan Burnsi, Joe Beda i Kelsey Hightowera