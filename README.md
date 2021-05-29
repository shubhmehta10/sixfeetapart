# 6' APART 
#### 6’ APART is an Android APP made for ensuring if the norms of Social Distancing are being followed by people in public places, or not. 

## PROBLEM STATEMENT 
One of the most effective measures to control the recent viral outbreak of the COVID-19 virus is adapting Social Distancing in everyday life. To comply with this constraint, governments are adopting restrictions over the minimum interpersonal distance between people (6 feet). Thus, it is crucial to measure the distance between people in crowded places after unlock is implemented.  
 
## PROPOSED SOLUTION  
For this, we introduce the 6’ APART APP, an estimation of the interpersonal distance between people. This is a feasible solution as it helps police personnel to find citizens not complying by the social distancing norms. The app is made using JAVA and contains a pretrained object detection model which analyzes the input video. 

## TECH STACK 
- Programming Languages: Java, XML, and Python. 
- Tools: Git, VSCode, Android Studio. 
- Framework: Django REST.  

## ARCHITECTURE 

The architecture of 6' APART APP is divided into three parts:
1) The frontend of the APP
2) The backend
3) Integration of the ML model used for object detection and tracking.

![architecture](https://user-images.githubusercontent.com/63549553/120079493-b73f2a00-c0d1-11eb-9625-b0c981cfa839.png)

## LOCAL SETUP 
```bash
git clone https://github.com/shubhmehta10/sixfeetapart.git
git checkout -b <branch-name>
```

```python
python manage.py migrate
python manage.py makemigrations
python manage.py runserver
```

-   Go to `http://127.0.0.1:8000/`

```bash
git add .
git commit -m "message"
git push origin <branch-name>
```

<!-- ### License 📜

- [MIT License](/LICENSE) -->

## _Authors_

Meet the team.

- Mihir Paghdal 
  - [LinkedIn](https://www.linkedin.com/in/mihir-paghdal)
- Parinda Pranami
  - [LinkedIn](https://www.linkedin.com/in/parindapranami)
- Shubh Mehta
  - [LinkedIn](https://www.linkedin.com/in/shubhmehta10/)

